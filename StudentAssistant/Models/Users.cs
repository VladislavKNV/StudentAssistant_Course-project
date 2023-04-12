using System;


namespace StudentAssistant.Models
{
	public class Users
	{
		public int id { get; set; }
		public int RoleId { get; set; }
		public string Login { get; set; }
		public string Email { get; set; }
		public string Password { get; set; }
	}
}