using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using StudentAssistant.Models;
using StudentAssistant.Repository;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace StudentAssistant.Controllers
{
	public class RestController : ApiController
    {
		//Создание объектов классов для подключения к бд
		RepositoryAuthentication repositoryAuthentication = new RepositoryAuthentication();


		[HttpPost]
		public HttpResponseMessage AuthenticateApi([FromBody] Users usersModel)
		{
			// Проверяем входные параметры
			if (string.IsNullOrEmpty(usersModel?.Login) || string.IsNullOrEmpty(usersModel?.Password))
			{
				return Request.CreateErrorResponse(HttpStatusCode.BadRequest, "Invalid login or password");
			}

			// Ищем пользователя в базе данных и забираем его данные 
			var user = repositoryAuthentication.Get(usersModel.Login);

			if (usersModel.Password != user.Password)
			{
				return Request.CreateErrorResponse(HttpStatusCode.BadRequest, "Invalid login or password");
			}

			string id = user.id.ToString();
			var subjectsList = new List<Subjects>();
			subjectsList = repositoryAuthentication.GetSubjects(id);
			var sessionList = new List<Session>();
			var labsList = new List<Labs>();


			foreach (var subject in subjectsList)
			{
				sessionList.AddRange(repositoryAuthentication.GetSessions(subject.SubjectId));
				labsList.AddRange(repositoryAuthentication.GetLabs(subject.SubjectId));
			}



			if (user == null || user.Password != usersModel.Password)
			{
				// Ошибка аутентификации
				return Request.CreateErrorResponse(HttpStatusCode.Unauthorized, "Invalid login or password");
			}

			// Формируем ответ с данными пользователя
			var responseData = new
			{
				UserModel = user,
				SubjectsList = subjectsList,
				LabsList = labsList,
				SessionList = sessionList,
			};

			// Возвращаем данные в формате JSON
			var response = Request.CreateResponse(HttpStatusCode.OK);
			response.Content = new StringContent(JsonConvert.SerializeObject(responseData), System.Text.Encoding.UTF8, "application/json");
			return response;
		}

		[HttpPost]
		public HttpResponseMessage RegistrationApi([FromBody] Users userModel)
		{
			if (repositoryAuthentication.Get(userModel.Login) != null) // Проверка наличия логина в базе данных
			{
				return new HttpResponseMessage(HttpStatusCode.BadRequest)
				{
					Content = new StringContent("Пользователь с таким логином уже существует")
				};
			}
			if (repositoryAuthentication.GetByEmail(userModel.Email) != null) // Проверка наличия email в базе данных
			{
				return new HttpResponseMessage(HttpStatusCode.BadRequest)
				{
					Content = new StringContent("Пользователь с таким email уже существует")
				};
			}

			// Создание пользователя
			int? UserId;
			UserId = repositoryAuthentication.Create(userModel.Login, userModel.Email, userModel.Password, userModel.RoleId);
			if (UserId != null)
			{
				var user = repositoryAuthentication.Get(userModel.Login);
				// Формируем ответ с данными пользователя
				var responseData = new
				{
					UserModel = user,
				};

				// Возвращаем данные в формате JSON
				var response = Request.CreateResponse(HttpStatusCode.OK);
				response.Content = new StringContent(JsonConvert.SerializeObject(responseData), System.Text.Encoding.UTF8, "application/json");
				return response;
			}
			else
			{
				return new HttpResponseMessage(HttpStatusCode.BadRequest)
				{
					Content = new StringContent("Error")
				};
			}
		}

		[HttpPost]
		public HttpResponseMessage AddSubjectApi([FromBody] Subjects subjectsModel)
		{
			if (repositoryAuthentication.GetSubjectsByName(subjectsModel.UserId.ToString(), subjectsModel.Name) != null)
			{
				return new HttpResponseMessage(HttpStatusCode.BadRequest)
				{
					Content = new StringContent("Предмет с таким именем уже существует")
				};
			}
			if (repositoryAuthentication.AddSubject(subjectsModel) == true) 
			{
				var subject = repositoryAuthentication.GetSubjectsByName(subjectsModel.UserId.ToString(), subjectsModel.Name);
				// Формируем ответ с данными пользователя
				var responseData = new
				{
					SubjectModel = subject,
				};

				// Возвращаем данные в формате JSON
				var response = Request.CreateResponse(HttpStatusCode.OK);
				response.Content = new StringContent(JsonConvert.SerializeObject(responseData), System.Text.Encoding.UTF8, "application/json");
				return response;
			} else
			{
				return new HttpResponseMessage(HttpStatusCode.BadRequest)
				{
					Content = new StringContent("Error")
				};
			}
		}

		[HttpPost]
		public HttpResponseMessage AddLabApi([FromBody] Labs labModel)
		{
			if (repositoryAuthentication.SubjectExists(labModel.SubjectsId) == false) 
			{
				return new HttpResponseMessage(HttpStatusCode.BadRequest)
				{
					Content = new StringContent("Предмета не существует")
				};
			}

			int id = repositoryAuthentication.AddLab(labModel);
			if (id != -1) 
			{
				var lab = repositoryAuthentication.GetLab(id);
				// Формируем ответ с данными пользователя
				var responseData = new
				{
					LabModel = lab,
				};

				// Возвращаем данные в формате JSON
				var response = Request.CreateResponse(HttpStatusCode.OK);
				response.Content = new StringContent(JsonConvert.SerializeObject(responseData), System.Text.Encoding.UTF8, "application/json");
				return response;
			}
			else
			{
				return new HttpResponseMessage(HttpStatusCode.BadRequest)
				{
					Content = new StringContent("Error")
				};
			}
		}

		[HttpPost]
		public HttpResponseMessage AddLabsApi([FromBody] JObject data)
		{
			int subjectId = data["SubjectId"].ToObject<int>();
			int count = data["Count"].ToObject<int>();
			if (repositoryAuthentication.SubjectExists(subjectId) == false)
			{
				return new HttpResponseMessage(HttpStatusCode.BadRequest)
				{
					Content = new StringContent("Предмета не существует")
				};
			}
			var labsList = new List<Labs>();
			int err = 0;
			for (int i = 0; i < count; i++)
			{
				int id = repositoryAuthentication.AddLabs(subjectId);
				if (id == -1)
				{
					err++;
				} else
				{
					labsList.Add(repositoryAuthentication.GetLab(id));
				}
			}

			if (err == 0) 
			{
				// Формируем ответ с данными пользователя
				var responseData = new
				{
					LabModel = labsList,
				};

				// Возвращаем данные в формате JSON
				var response = Request.CreateResponse(HttpStatusCode.OK);
				response.Content = new StringContent(JsonConvert.SerializeObject(responseData), System.Text.Encoding.UTF8, "application/json");
				return response;
			}
			else
			{
				return new HttpResponseMessage(HttpStatusCode.BadRequest)
				{
					Content = new StringContent("Error")
				};
			}
		}


		[HttpPut]
		public IHttpActionResult UpdateLabApi([FromBody] Labs labModel)
		{
			if (repositoryAuthentication.UpdateLabs(labModel))
			{
				return Ok("ok");
			}
			else
			{
				return BadRequest("error");
			}
		}

		[HttpPut]
		public IHttpActionResult UpdateSubjectApi([FromBody] Subjects subjectsModel)
		{
			if (repositoryAuthentication.UpdateSubjects(subjectsModel))
			{
				return Ok("ok");
			}
			else
			{
				return BadRequest("error");
			}
		}

		[HttpDelete]
		public IHttpActionResult DeleteLabApi([FromBody] Labs labModel) //только id
		{
			if (repositoryAuthentication.DeleteLab(labModel.LabsId))
			{
				return Ok("ok");
			}
			else
			{
				return BadRequest("error");
			}
		}

		[HttpDelete]
		public IHttpActionResult DeleteSubjectApi([FromBody] Subjects subjectsModel) //только id
		{
			if (repositoryAuthentication.DeleteSubject(subjectsModel.SubjectId))
			{
				return Ok("ok");
			}
			else
			{
				return BadRequest("error");
			}
		}

	}
}
