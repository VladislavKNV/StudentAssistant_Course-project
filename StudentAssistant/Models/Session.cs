using System;			

namespace StudentAssistant.Models
{
	public class Session
	{
		public int SessionId { get; set; }
		public int SubjectId { get; set; }
		public string Type { get; set; }
		public string Status { get; set; }
		public int Mark { get; set; }
		public string DateTime { get; set; }
		public string Auditorium { get; set; }
	}
}