﻿using StudentAssistant.Models;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Web;
using System.Web.Helpers;
using System.Web.Security;
using System.Web.UI.WebControls;

namespace StudentAssistant.Repository
{
	public class RepositoryAuthentication
	{
		//Строка подключение к бд
		string connectionString = "Server=93.125.10.36;Database=StudenAssistant;Integrated Security=false;User Id=Vlad;Password=50511007;";
		//string connectionString = "Data Source=(local);Initial Catalog=StudentAssistant;Integrated Security=true";
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

			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
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

		public Users GetUserById(int id)
		{
			Users user = null;
			var queryString = "select top 1 id,RoleId,Login,Email,Password from Users where id=@id";//sql запрос

			// Создание и открытие соединения в блоке using.
			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@id", id);
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
					}
				}
				return user;
			}
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

		public List<Subjects> GetSubjectsForSession(string userId)
		{
			var subjectsList = new List<Subjects>();
			var queryString = "SELECT s.id, s.UserId, s.Name " +
				  "FROM Subjects s " +
				  "JOIN Session se ON s.id = se.SubjectId " +
				  "WHERE s.UserId = @UserId AND se.Status = 'Допущен' AND s.id IN " +
				  "(SELECT id FROM Subjects WHERE UserId = @UserId)";

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

		public Subjects GetSubjectsByName(string userId, string name)
		{
			Subjects subject = null;
			var queryString = "select id, UserId, Name from Subjects where UserId=@UserId and Name = @Name";

			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@UserId", userId);
				command.Parameters.AddWithValue("@Name", name);

				connection.Open();

				using (var reader = command.ExecuteReader())
				{
					while (reader.Read())
					{
						subject= new Subjects();
						object dbVal = null;

						subject.SubjectId = (int)reader.GetValue(0);
						subject.UserId = (int)reader.GetValue(1);

						dbVal = reader.GetValue(2);
						if (!(dbVal is DBNull))
						{
							subject.Name = (dbVal as string).Trim();
						}

					}
				}
			}

			return subject;
		}

		public bool SubjectExists(int subjectId)
		{
			var queryString = "select count(*) from Subjects where id=@SubjectId";

			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@SubjectId", subjectId);

				connection.Open();

				var count = (int)command.ExecuteScalar();

				return count > 0;
			}
		}


		public List<Labs> GetLabs(int subjectsId)
		{
			var labsList = new List<Labs>();
			var queryString = "select id, SubjectId, LabProtected from Labs where SubjectId=@SubjectId";

			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@SubjectId", subjectsId);
				connection.Open();

				using (var reader = command.ExecuteReader())
				{
					while (reader.Read())
					{
						var lab = new Labs();
						lab.LabsId = (int)reader.GetValue(0);
						lab.SubjectsId = (int)reader.GetValue(1);
						lab.LabProtected = (int)reader.GetValue(2);

						labsList.Add(lab);
					}
				}
			}

			return labsList;
		}

		public Labs GetLab(int labId)
		{
			Labs lab = null;
			var queryString = "select id, SubjectId, LabProtected from Labs where id=@labId";

			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@labId", labId);
				connection.Open();

				using (var reader = command.ExecuteReader())
				{
					while (reader.Read())
					{
						lab = new Labs();
						lab.LabsId = (int)reader.GetValue(0);
						lab.SubjectsId = (int)reader.GetValue(1);
						lab.LabProtected = (int)reader.GetValue(2);
					}
				}
			}

			return lab;
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

		public Session GetSessionById(int sessionId)
		{
			Session session = null;
			var queryString = "select id, SubjectId, Type, Status, Mark, DateTime, Auditorium from Session where id=@id";

			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@id", sessionId);
				connection.Open();

				using (var reader = command.ExecuteReader())
				{
					while (reader.Read())
					{
						object dbVal = null;

						session = new Session();
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
					}
				}
			}

			return session;
		}


		public bool UpdateMark(int Mark, int subjectId)
		{
			var session = new List<Session>();
			session = GetSessions(subjectId);

			if (session != null)
			{
				var queryString = "Update Session Set Mark = @Mark where SubjectId = @subjectId";
				using (SqlConnection connection = new SqlConnection(connectionString))
				{
					foreach (var item in session)
					{
						// Create the Command and Parameter objects.
						var command = new SqlCommand(queryString, connection);
						command.Parameters.AddWithValue("@SubjectId", item.SubjectId);
						command.Parameters.AddWithValue("@Mark", Mark);


						connection.Open();
						var reader = command.ExecuteReader();
						reader.Read();
					}
				}
				return true;
			}
			else
			{
				return false;
			}
		}

		public bool UpdateSession(string dateTime, string auditorium, int subjectId)
		{
			var session = new List<Session>();
			session = GetSessions(subjectId);

			if (session != null)
			{
				var queryString = "Update Session Set DateTime = @DateTime, Auditorium = @Auditorium where SubjectId = @SubjectId";
				using (SqlConnection connection = new SqlConnection(connectionString))
				{
					foreach (var item in session)
					{
						// Create the Command and Parameter objects.
						var command = new SqlCommand(queryString, connection);
						command.Parameters.AddWithValue("@SubjectId", item.SubjectId);
						command.Parameters.AddWithValue("@DateTime", dateTime);
						command.Parameters.AddWithValue("@Auditorium", auditorium);

						connection.Open();
						var reader = command.ExecuteReader();
						reader.Read();
					}
				}
				return true;
			}
			else
			{
				return false;
			}
		}

		public bool UpdateSession2(int id, int subjectId, string Type, string Status)
		{
			var session = new List<Session>();
			session = GetSessions(subjectId);

			if (session != null)
			{
				var queryString = "Update Session Set Type = @Type, Status = @Status where id = @id";
				using (SqlConnection connection = new SqlConnection(connectionString))
				{
					foreach (var item in session)
					{
						// Create the Command and Parameter objects.
						var command = new SqlCommand(queryString, connection);
						command.Parameters.AddWithValue("@id", id);
						command.Parameters.AddWithValue("@Type", Type);
						command.Parameters.AddWithValue("@Status", Status);

						connection.Open();
						var reader = command.ExecuteReader();
						reader.Read();
					}
				}
				return true;
			}
			else
			{
				return false;
			}
		}

		public bool UpdateUser(int id, int RoleId)
		{
			var queryString = "Update Users Set RoleId = @RoleId where id = @id";
			using (SqlConnection connection = new SqlConnection(connectionString))
			{

				// Create the Command and Parameter objects.
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@id", id);
				command.Parameters.AddWithValue("@RoleId", RoleId);

				connection.Open();
				var reader = command.ExecuteReader();
				reader.Read();

			}
			return true;
		}
			

		public bool DeleteUser(int userId)
		{
			var queryString = " Delete from Users where id = @id";//sql запрос
			using (SqlConnection connection = new SqlConnection(connectionString))
			{
				// Create the Command and Parameter objects.
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@id", userId);
				connection.Open();
				var reader = command.ExecuteReader();
				reader.Read();
			}
			return true;
		}


		///for Rest Api ///////////////

		public bool AddSubject(Subjects subject)
		{
			var queryString = "INSERT INTO Subjects (UserId, Name) VALUES (@UserId, @Name)";

			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@UserId", subject.UserId);
				command.Parameters.AddWithValue("@Name", subject.Name);

				connection.Open();

				command.ExecuteNonQuery();
			}
			return true;
		}

		public int AddLab(Labs labs)
		{
			var queryString = "INSERT INTO Labs (SubjectId, LabProtected) OUTPUT INSERTED.Id VALUES (@SubjectId, @LabProtected)";

			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@SubjectId", labs.SubjectsId);
				command.Parameters.AddWithValue("@LabProtected", labs.LabProtected);

				connection.Open();

				var result = command.ExecuteScalar();
				if (result == null)
				{
					return -1;
				}

				return (int)result;
			}
		}

		public int AddLabs(int SubjectsId)
		{
			var queryString = "INSERT INTO Labs (SubjectId, LabProtected) OUTPUT INSERTED.Id VALUES (@SubjectId, @LabProtected)";

			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@SubjectId", SubjectsId);
				command.Parameters.AddWithValue("@LabProtected", 0);

				connection.Open();

				var result = command.ExecuteScalar();
				if (result == null)
				{
					return -1;
				}

				return (int)result;
			}
		}

		public int AddSession(Session session)
		{
			var queryString = "INSERT INTO Session (SubjectId, Type, Status) OUTPUT INSERTED.Id VALUES (@SubjectId, @Type, @Status)";

			using (var connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@SubjectId", session.SubjectId);
				command.Parameters.AddWithValue("@Type", session.Type);
				command.Parameters.AddWithValue("@Status", session.Status);

				connection.Open();

				var result = command.ExecuteScalar();
				if (result == null)
				{
					return -1;
				}

				return (int)result;
			}
		}

		public bool UpdateLabs(Labs labs)
		{
			var queryString = "Update Labs Set LabProtected = @LabProtected where id = @id";
			using (SqlConnection connection = new SqlConnection(connectionString))
			{

				// Create the Command and Parameter objects.
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@id", labs.LabsId);
				command.Parameters.AddWithValue("@LabProtected", labs.LabProtected);

				connection.Open();
				var reader = command.ExecuteReader();
				reader.Read();

			}
			return true;
		}

		public bool UpdateSubjects(Subjects subjects)
		{
			var queryString = "Update Subjects Set Name = @Name where id = @id";
			using (SqlConnection connection = new SqlConnection(connectionString))
			{

				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@id", subjects.SubjectId);
				command.Parameters.AddWithValue("@Name", subjects.Name);

				connection.Open();
				var reader = command.ExecuteReader();
				reader.Read();

			}
			return true;
		}


		public bool DeleteSubject(int subjectId)
		{
			var queryString = " Delete from Subjects where id = @id";
			using (SqlConnection connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@id", subjectId);
				connection.Open();
				var reader = command.ExecuteReader();
				reader.Read();
			}
			return true;
		}

		public bool DeleteLab(int labId)
		{
			var queryString = " Delete from Labs where id = @id";
			using (SqlConnection connection = new SqlConnection(connectionString))
			{
				var command = new SqlCommand(queryString, connection);
				command.Parameters.AddWithValue("@id", labId);
				connection.Open();
				var reader = command.ExecuteReader();
				reader.Read();
			}
			return true;
		}

	}
}