using System;
using System.Collections.Generic;

namespace CWA.TestResultsDB.Models
{
    public partial class TestResultsEntity
    {
        public Guid Id { get; set; }
        public string ResultId { get; set; }
        public DateTime ResultDate { get; set; }
        public int Result { get; set; }
    }
}
