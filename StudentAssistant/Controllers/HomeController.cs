using StudentAssistant.Models;
using StudentAssistant.Repository;
using System;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.Mvc;
using System.Web.UI.WebControls;

namespace StudentAssistant.Controllers
{
    public class HomeController : Controller
    {
        //Создание объектов классов для подключения к бд
        RepositoryAuthentication repositoryAuthentication = new RepositoryAuthentication();

        //переменные для работы с даными
        int? UserId;


        public ActionResult _Layout()
        {
            return View();
        }

        public ActionResult Registration()
        {
            return View();
        }

        public ActionResult Autorisation()
        {
            return View();
        }

		public ActionResult MainPage()
		{
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
				Data.Expires = DateTime.Now.AddMinutes(30);
				Response.Cookies.Add(Data);
				if (Data == null)
				{
					return View("Registration");
				}
				else
				{
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
				Data.Expires = DateTime.Now.AddMinutes(30);
				Response.Cookies.Add(Data);
				if (Data == null)
				{
					return View("Registration");
				}
				else
				{
					return View("MainPage");
				}
            }
			else
			{
				ViewBag.login = login;
				ViewBag.error = "Неверный логин или пароль";
				return View(this);
			}
		}

	}
}