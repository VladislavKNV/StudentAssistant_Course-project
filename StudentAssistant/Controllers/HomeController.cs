using Microsoft.Ajax.Utilities;
using Newtonsoft.Json.Linq;
using StudentAssistant.Models;
using StudentAssistant.Repository;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Globalization;
using System.Security.Cryptography;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.Helpers;
using System.Web.Mvc;
using System.Web.UI.WebControls;
using System.Xml.Linq;

namespace StudentAssistant.Controllers
{
    public class HomeController : Controller
    {
        //Создание объектов классов для подключения к бд
        RepositoryAuthentication repositoryAuthentication = new RepositoryAuthentication();

        //переменные для работы с даными
        int? UserId;
		string error;


		public ActionResult _Layout()
        {
            return View();
        }

        public ActionResult Registration()
        {
			ViewBag.checkInput = false;
			return View();
        }

        public ActionResult Autorisation()
        {
			//Очищение cooki
			HttpCookie cookie = new HttpCookie("Data");
			cookie.Expires = DateTime.Now.AddDays(-1);
			Response.Cookies.Add(cookie);
			ViewBag.checkInput = false;
			return View();
        }

		public ActionResult MainPage()
		{
			var dataCookie = Request.Cookies["Data"];
			if (dataCookie == null)
			{
				ViewBag.checkInput = false;
			} else
			{
				ViewBag.checkInput = true;
			}
			return View();
		}

		[HttpPost]
		public ActionResult registration(string login, string email, string password, string confirm_password)
		{
			bool valid = true;
			// Валидация логина
			if (string.IsNullOrEmpty(login))
			{
				ViewBag.ErrorLogin = "Логин не может быть пустым";
				valid = false;
			}
			else if (login.Length < 3 || login.Length > 25)
			{
				ViewBag.ErrorLogin = "Логин должен содержать от 3 до 25 символов";
				valid = false;
			}
			else if (repositoryAuthentication.Get(login) != null) // Проверка наличия логина в базе данных
			{
				ViewBag.ErrorLogin = "Пользователь с таким логином уже существует";
				valid = false;
			}

			// Валидация email
			if (string.IsNullOrEmpty(email))
			{
				ViewBag.ErrorEmail = "Email не может быть пустым";
				valid = false;
			}
			else if (repositoryAuthentication.GetByEmail(email) != null) // Проверка наличия email в базе данных
			{
				ViewBag.ErrorEmail = "Пользователь с таким email уже существует";
				valid = false;
			}
			else if (!IsValidEmail(email))
			{
				ViewBag.ErrorEmail = "Неправильный формат email";
				valid = false;
			}

			// Валидация пароля
			if (string.IsNullOrEmpty(password))
			{
				ViewBag.ErrorPassword = "Пароль не может быть пустым";
				valid = false;
			}
			else if (password.Length < 6 || password.Length > 25)
			{
				ViewBag.ErrorPassword = "Пароль должен содержать от 6 до 25 символов";
				valid = false;
			} 
			else if (password != confirm_password)// Проверка подтверждения пароля
			{
				ViewBag.ErrorPassword = "Пароли не совпадют";
				valid = false;
			}

			if (valid == false)
			{
				ViewBag.login = login;
				ViewBag.email = email;
				return View(this);
			}

			// Создание пользователя
			UserId = repositoryAuthentication.Create(login, email, password, 2);
			if (UserId == null)
			{
				ViewBag.error = "Пользователь не создан";
				ViewBag.login = login;
				ViewBag.email = email;
				return View(this);
			}
			else
			{
				// Заполнение таблицы
				// Очищение cooki
				HttpCookie cookie = new HttpCookie("Data");
				cookie.Expires = DateTime.Now.AddDays(-1);
				Response.Cookies.Add(cookie);

				Users user1 = repositoryAuthentication.Get(login);

				HttpCookie Data = new HttpCookie("Data");
				// Добавление cookies
				Data.Values.Add("Id", user1.id.ToString());
				Data.Values.Add("Login", user1.Login.ToString());
				Data.Values.Add("Email", user1.Email.ToString());
				Data.Values.Add("Password", user1.Password.ToString());
				Data.Values.Add("RoleId", user1.RoleId.ToString());
				Data.Expires = DateTime.Now.AddMinutes(60);
				Response.Cookies.Add(Data);
				if (Data == null)
				{
					ViewBag.checkInput = false;
					return View("Registration");
				}
				else
				{
					ViewBag.checkInput = true;
					return View("MainPage");
				}
			}
		}

		private bool IsValidEmail(string email)
		{
			if (string.IsNullOrEmpty(email))
			{
				return false;
			}

			string pattern = @"^([\w\.\-]+)@([\w\-]+)((\.(\w){2,})+)$";
			Regex regex = new Regex(pattern, RegexOptions.IgnoreCase);

			return regex.IsMatch(email);
		}

		[HttpPost]
        public ActionResult autorisation(string login, string password)
        {
            Users user = repositoryAuthentication.Get(login);

			// Валидация логина
			if (string.IsNullOrEmpty(login))
			{
				ViewBag.login = login;
				ViewBag.ErrorLogin = "Логин не может быть пустым";
				return View(this);
			}

			// Проверка наличия логина в базе данных
			if (repositoryAuthentication.Get(login) == null)
			{
				ViewBag.login = login;
				ViewBag.error = "Неверный логин или пароль";
				return View(this);
			}

			// Валидация пароля
			if (string.IsNullOrEmpty(password))
			{
				ViewBag.login = login;
				ViewBag.error = "Пароль не может быть пустым";
				return View(this);
			}

			if (Convert.ToString(repositoryAuthentication.GetHash(password)) == user.Password)
            {
				Users user1 = repositoryAuthentication.Get(login);
				//Заполнение таблицы
				//Очищение cooki
				HttpCookie cookie = new HttpCookie("Data");
				cookie.Expires = DateTime.Now.AddDays(-1);
				Response.Cookies.Add(cookie);


				HttpCookie Data = new HttpCookie("Data");
				//Add Cookies
				Data.Values.Add("Id", user1.id.ToString());
				Data.Values.Add("Login", user1.Login.ToString());
				Data.Values.Add("Email", user1.Email.ToString());
				Data.Values.Add("Password", user1.Password.ToString());
				Data.Values.Add("RoleId", user1.RoleId.ToString());
				Data.Expires = DateTime.Now.AddMinutes(60);
				Response.Cookies.Add(Data);
				if (Data == null)
				{
					ViewBag.checkInput = false;
					return View("Registration");
				}
				else
				{
					ViewBag.checkInput = true;
					return View("MainPage");
				}
            }
			else
			{
				ViewBag.login = login;
				ViewBag.error = "Неверный логин или пароль";
				ViewBag.checkInput = false;
				return View(this);
			}
		}

		[HttpGet]
		public ActionResult PersonalAccount(string error, string nameSubject, string subjectMark, string SubjectSessiom, string dateTime, string auditorium)
		{

			// Получение куки с именем "Data"
			var dataCookie = Request.Cookies["Data"];

			if (dataCookie != null)
			{
				// Получение значений из куки
				var id = dataCookie.Values["Id"];
				var login = dataCookie.Values["Login"];
				var email = dataCookie.Values["Email"];
				var password = dataCookie.Values["Password"];
				var roleId = dataCookie.Values["RoleId"];

				if (roleId == "2")
				{
					ViewBag.role = "2";

					bool Status = true;
					int countExam = 0;
					int countOffset = 0;
					int sumMark = 0;
					int countMark = 0;
					int countAllLabs = 0;
					int countAllProtectedLabs = 0;
					var subjectsList = new List<Subjects>();
					var subjectsListForMark = new List<Subjects>();
					var sessionList = new List<Session>();
					var labsList = new ArrayList();
					subjectsList = repositoryAuthentication.GetSubjects(id);
					subjectsListForMark = repositoryAuthentication.GetSubjectsForSession(id);

					foreach (var subject in subjectsList)
					{
						sessionList.AddRange(repositoryAuthentication.GetSessions(subject.SubjectId));
						int countLabs = repositoryAuthentication.GetLabsCount(subject.SubjectId);
						int countProtectedLabs = repositoryAuthentication.GetProtectedLabsCount(subject.SubjectId);
						string progressSubject = countProtectedLabs + "/" + countLabs;
						labsList.Add(progressSubject);
						countAllLabs += countLabs;
						countAllProtectedLabs += countProtectedLabs;
					}
					foreach (var session in sessionList)
					{
						if (session.Type == "Экзамен")
						{
							countExam++;
						}
						else { countOffset++; }
						if (session.Mark != 0) { countMark++; }
						sumMark += session.Mark;
						if (session.Status == "Не допущен")
						{
							Status = false;
						}
					}
					double percent = ((double)countAllProtectedLabs / (double)countAllLabs) * 100;
					percent = Math.Round(percent, 2);
					string formattedPercent = percent.ToString();

					//----------------------------------------------
					if (countMark != 0)
					{
						ViewBag.averageMark = sumMark / countMark;
					}
					else { ViewBag.averageMark = 0; }

					if (Status == true)
					{
						ViewBag.Status = "допущен";
					}
					else {
						ViewBag.Status = "не допущен"; 
					}
					if (countAllLabs == 0)
					{
						string formatedProgress = "-";
						ViewBag.formatedProgress = formatedProgress;
						ViewBag.Status = " ";
					}
					else
					{
						string formatedProgress = countAllProtectedLabs + "/" + countAllLabs + " (" + formattedPercent + "%)";
						ViewBag.formatedProgress = formatedProgress;
					}

					ViewBag.subjectListForMark = subjectsListForMark;
					ViewBag.checkInput = true;
					ViewBag.countExam = countExam;
					ViewBag.countOffset = countOffset;
					ViewBag.login = login;
					ViewBag.subjectList = subjectsList;
					ViewBag.sessionList = sessionList;
					ViewBag.labList = labsList;
					if (error != null && nameSubject != null && subjectMark != null)
					{
						ViewBag.Error = error;
						ViewBag.nameSubject = nameSubject;
						ViewBag.subjectMark = subjectMark;
					}
					if (error != null && SubjectSessiom != null && dateTime != null && auditorium != null)
					{
						ViewBag.ErrorSession = error;
						ViewBag.SubjectSessiom = SubjectSessiom;
						ViewBag.dateTime = dateTime;
						ViewBag.auditorium = auditorium;
					}

				}
				else if (roleId == "1")
				{
					ViewBag.checkInput = true;
					ViewBag.role = "1";
					var usersList = new List<Users>();
					usersList = repositoryAuthentication.GetUsers();
					ViewBag.usersList = usersList;
				}
			}else
			{
				ViewBag.checkInput = false;
				return RedirectToAction("Registration");
			}
			return View(this);
		}


		//добавить проверку на допуск в гет PersonalAccountAdmin
		public ActionResult PersonalAccountAddMark(string nameSubject, string subjectMark)
		{

			// Получение куки с именем "Data"
			var dataCookie = Request.Cookies["Data"];

			if (dataCookie != null)
			{
				// Получение значений из куки
				var id = dataCookie.Values["Id"];

				int Mark = 0;

				try
				{
					Mark = Convert.ToInt32(subjectMark);
					if (Mark >= 1 && Mark <= 10)
					{	
					}
					else
					{
						error = "\"Неверный формат оценки";
						return RedirectToAction("PersonalAccount", "Home", new { error = error, nameSubject = nameSubject, subjectMark = subjectMark });
					}
				}
				catch (FormatException)
				{
					ViewBag.Error = "Неверный формат оценки";
				}

				if (Mark > 0 && Mark <= 10)
				{
					Subjects subject = new Subjects();
					subject = repositoryAuthentication.GetSubjectsByName(id, nameSubject);

					if (subject.SubjectId >= 0)
					{
						if (repositoryAuthentication.UpdateMark(Mark, subject.SubjectId) == true)
						{
							ViewBag.checkInput = true;
							return RedirectToAction("PersonalAccount", "Home", new { area = "" });
						}
					}
					else
					{
						error = "Произошла ошибка";
						return RedirectToAction("PersonalAccount", "Home", new { error = error, nameSubject = nameSubject, subjectMark = subjectMark });
					}
				}
				else
				{
					error = "Неверный формат оценки";
				}
			}
			return RedirectToAction("PersonalAccount", "Home", new { error = error, nameSubject = nameSubject, subjectMark = subjectMark });
		}



		public ActionResult PersonalAccountUpdateSession(string SubjectSessiom, string dateTime, string auditorium)
		{

			// Получение куки с именем "Data"
			var dataCookie = Request.Cookies["Data"];

			if (dataCookie != null)
			{
				// Получение значений из куки
				var id = dataCookie.Values["Id"];

				string format = "dd.MM.yyyy HH:mm";
				DateTime result;

				if (DateTime.TryParseExact(dateTime, format, CultureInfo.InvariantCulture, DateTimeStyles.None, out result))
				{
					Subjects subject = new Subjects();
					subject = repositoryAuthentication.GetSubjectsByName(id, SubjectSessiom);

					if (subject.SubjectId >= 0)
					{
						if (repositoryAuthentication.UpdateSession(dateTime, auditorium, subject.SubjectId) == true)
						{
							ViewBag.checkInput = true;
							return RedirectToAction("PersonalAccount", "Home", new { area = "" });
						}
						else
						{
							error = "Произошла ошибка";
							return RedirectToAction("PersonalAccount", "Home", new { error = error, SubjectSessiom = SubjectSessiom, dateTime = dateTime, auditorium = auditorium });
						}
					}
					else
					{
						error = "Произошла ошибка";
						return RedirectToAction("PersonalAccount", "Home", new { error = error, SubjectSessiom = SubjectSessiom, dateTime = dateTime, auditorium = auditorium });
					}
				}
				else
				{
					error = "Неверный формат данных[";
				}
			}
			return RedirectToAction("PersonalAccount", "Home", new { error = error, SubjectSessiom = SubjectSessiom, dateTime = dateTime, auditorium = auditorium });
		}




		public ActionResult PersonalAccountAdminDel(string SelectedItem)
		{
			// Получение куки с именем "Data"
			var dataCookie = Request.Cookies["Data"];

			if (dataCookie != null)
			{
				// Получение значений из куки
				int id = Convert.ToInt32(dataCookie.Values["Id"]);


				int IdDel = Convert.ToInt32(SelectedItem);
				if (id != IdDel)
				{
					if (repositoryAuthentication.Delete(IdDel) == true)
					{
						return RedirectToAction("PersonalAccount", "Home", new { });
					}
					else
					{
						//тут будет ошибка
						return RedirectToAction("PersonalAccount", "Home", new { });
					}
				}
			} else
			{
				//тут будет ошибка
				return RedirectToAction("PersonalAccount", "Home", new { });
			}
			return RedirectToAction("PersonalAccount", "Home", new {});
		}

		public ActionResult PersonalAccountAdminRole(string SelectedItem2)
		{
			// Получение куки с именем "Data"
			var dataCookie = Request.Cookies["Data"];

			if (dataCookie != null)
			{
				// Получение значений из куки
				int id = Convert.ToInt32(dataCookie.Values["Id"]);

				if (SelectedItem2 == "" || SelectedItem2 == null)
				{
					//error
				}
				else
				{
					int IdUser = Convert.ToInt32(SelectedItem2);//добавить ошибку если ничего не выбрано

					if (id != IdUser)
					{
						Users user = new Users();
						user = repositoryAuthentication.GetUserById(IdUser);

						if (user.RoleId == 2)
						{
							if (repositoryAuthentication.UpdateUser(IdUser, 1) == true)
							{
								return RedirectToAction("PersonalAccount", "Home", new { });
							}
							else
							{
								//тут будет ошибка
								return RedirectToAction("PersonalAccount", "Home", new { });
							}
						}
						else if (user.RoleId == 1)
						{
							if (repositoryAuthentication.UpdateUser(IdUser, 2) == true)
							{
								return RedirectToAction("PersonalAccount", "Home", new { });
							}
							else
							{
								//тут будет ошибка
								return RedirectToAction("PersonalAccount", "Home", new { });
							}
						}
					}
				}
			}
			else
			{
				//тут будет ошибка
				return RedirectToAction("PersonalAccount", "Home", new { });
			}
			return RedirectToAction("PersonalAccount", "Home", new { });
		}


	}
}