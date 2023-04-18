using StudentAssistant.Models;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Web;
using System.Web.UI.WebControls;

namespace StudentAssistant.Repository
{
	public class RepositoryAuthentication
	{
		//Строка подключение к бд
		//string connectionString = "Server=93.125.10.36;Database=Students;Integrated Security=false;User Id=User;Password=2001ksu2001;";
		string connectionString = "Data Source=(local);Initial Catalog=StudentAssistant;Integrated Security=true";
		//Хеширование
		/// <summary>
		/// 
		/// </summary>
		/// <param name="input"></param>
		/// <returns></returns>
		public string GetHash(string input)
		{
			MD5 md5Hash = MD5.Create();
			// Convert the input string to a byte array and compute the hash.
			byte[] data = md5Hash.ComputeHash(Encoding.UTF8.GetBytes(input));

			// Create a new Stringbuilder to collect the bytes
			// and create a string.
			StringBuilder sBuilder = new StringBuilder();

			// Loop through each byte of the hashed data 
			// and format each one as a hexadecimal string.
			for (int i = 0; i < data.Length; i++)
			{
				sBuilder.Append(data[i].ToString("x2"));
			}

			// Return the hexadecimal string.
			return sBuilder.ToString();
		}

		public int? Create(
			string Login,
			string Email,
			string Password,
			int RoleId)
		{

			int? userId = null;
			var queryString =
"INSERT INTO [Users] ([Login] ,[Password] ,[Email] ,[RoleId]) OUTPUT Inserted.ID VALUES (@Login ,@Password,@Email,@RoleId)";//sql запрос

			// Создание и открытие соединения в блоке using.
			using (SqlConnection connection =
				new SqlConnection(connectionString))
			{
				//Создание объеков команд и параметров.
				using (var command = new SqlCommand(queryString, connection))
				{
					command.Parameters.AddWithValue("@Login", Login);
					command.Parameters.AddWithValue("@Password", GetHash(Password));
					command.Parameters.AddWithValue("@Email", Email);
					command.Parameters.AddWithValue("@RoleId", RoleId);
					//Открытие подключения
					connection.Open();
					userId = (int?)command.ExecuteScalar();
				}
			}
			return userId;
		}

		/// <summary>
		/// Вход
		/// </summary>
		/// <param name="Login">Login пользователя</param>
		/// <returns></returns>
		/// 

		public Users Get(string Login)
		{
			Users user = null;
			var queryString = "select top 1 id,RoleId,Login,Email,Password from Users where Login=@Login";//sql запрос

			// Создание и открытие соединения в блоке using.
			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@Login", Login);
				// Создание объеков команд и параметров.
				connection.Open();

				using (var reader = command.ExecuteReader())
				{
					if (reader.Read())
					{
						object dbVal = null;

						user = new Users();
						user.id = (int)reader.GetValue(0);

						user.RoleId = (int)reader.GetValue(1);

						dbVal = reader.GetValue(2);
						if (!(dbVal is DBNull))
						{
							user.Login = (dbVal as string).Trim();
						}

						dbVal = (string)reader.GetValue(3);
						if (!(dbVal is DBNull))
						{
							user.Email = (dbVal as string).Trim();
						}

						dbVal = (string)reader.GetValue(4);
						if (!(dbVal is DBNull))
						{
							user.Password = (dbVal as string).Trim();
						}

						
					}
				}
			}
			return user;
		}

		public Users GetByEmail(string Email)//фигня, но сойдет. Если будет не лень, то переделать 
		{
			Users user = null;
			var queryString = "select top 1 id,RoleId,Login,Email,Password from Users where Email=@Email";//sql запрос

			// Создание и открытие соединения в блоке using.
			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@Email", Email);
				// Создание объеков команд и параметров.
				connection.Open();

				using (var reader = command.ExecuteReader())
				{
					if (reader.Read())
					{
						user = new Users();
						user.id = (int)reader.GetValue(0);
					}
				}
				return user;
			}
				
		}


		public List<Users> GetUsers()
		{
			var usersList = new List<Users>();
			var queryString = "select id,RoleId,Login,Email from Users";

			// Создание и открытие соединения в блоке using.
			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				// Создание объеков команд и параметров.
				connection.Open();

				using (var reader = command.ExecuteReader())
				{
					while (reader.Read())
					{
						object dbVal = null;

						var user = new Users();
						user.id = (int)reader.GetValue(0);

						user.RoleId = (int)reader.GetValue(1);

						dbVal = reader.GetValue(2);
						if (!(dbVal is DBNull))
						{
							user.Login = (dbVal as string).Trim();
						}

						dbVal = (string)reader.GetValue(3);
						if (!(dbVal is DBNull))
						{
							user.Email = (dbVal as string).Trim();
						}

						usersList.Add(user);
					}
				}
			}
			return usersList;
		}


		public List<Subjects> GetSubjects(string userId)
		{
			var subjectsList = new List<Subjects>();
			var queryString = "select id, UserId, Name from Subjects where UserId=@UserId";

			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@UserId", userId);

				connection.Open();

				using (var reader = command.ExecuteReader())
				{
					while (reader.Read())
					{
						object dbVal = null;

						var subject = new Subjects();
						subject.SubjectId = (int)reader.GetValue(0);
						subject.UserId = (int)reader.GetValue(1);

						dbVal = reader.GetValue(2);
						if (!(dbVal is DBNull))
						{
							subject.Name = (dbVal as string).Trim();
						}

						subjectsList.Add(subject);
					}
				}
			}

			return subjectsList;
		}




		public int GetLabsCount(int subjectsId)
		{
			var queryString = "select count(*) from Labs where SubjectId=@SubjectId";
			var count = 0;

			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@SubjectId", subjectsId);

				connection.Open();

				count = (int)command.ExecuteScalar();
			}

			return count;
		}

		public int GetProtectedLabsCount(int subjectsId)
		{
			var queryString = "select count(*) from Labs where SubjectId=@SubjectId and LabProtected=1";
			var count = 0;

			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@SubjectId", subjectsId);

				connection.Open();

				count = (int)command.ExecuteScalar();
			}

			return count;
		}


		public List<Session> GetSessions(int subjectsId)
		{
			var sessionList = new List<Session>();
			var queryString = "select id, SubjectId, Type, Status, Mark, DateTime, Auditorium from Session where SubjectId=@SubjectId";

			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@SubjectId", subjectsId);

				connection.Open();

				using (var reader = command.ExecuteReader())
				{
					while (reader.Read())
					{
						object dbVal = null;

						var session = new Session();
						session.SessionId = (int)reader.GetValue(0);
						session.SubjectId = (int)reader.GetValue(1);

						dbVal = reader.GetValue(2);
						if (!(dbVal is DBNull))
						{
							session.Type = (dbVal as string).Trim();
						}

						dbVal = reader.GetValue(3);
						if (!(dbVal is DBNull))
						{
							session.Status = (dbVal as string).Trim();
						}

						dbVal = reader.GetValue(4);
						if (!(dbVal is DBNull))
						{
							session.Mark = reader.GetInt32(4);
						}

						dbVal = reader.GetValue(5);
						if (!(dbVal is DBNull))
						{
							session.DateTime = (dbVal as string).Trim();
						}

						dbVal = reader.GetValue(6);
						if (!(dbVal is DBNull))
						{
							session.Auditorium = (dbVal as string).Trim();
						}

						sessionList.Add(session);
					}
				}
			}

			return sessionList;
		}



	}
}