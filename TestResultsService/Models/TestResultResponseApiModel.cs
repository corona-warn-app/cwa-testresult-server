using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CWA.TestResultsService.Models
{
    /// <summary>
    /// 
    /// </summary>
    public class TestResultResponseApiModel
    {
        /// <summary>
        /// The test result state reported by the test lab:<br/>
        ///    Pending  = 0 : The test result does not exist yet<br/>
        ///    Negative = 1 : No indication for COVID-19<br/>
        ///    Positive = 2 : The test result indicates infection with COVID-19<br/>
        ///    Invalid  = 3 : The test result is invalid due to unknown reason
        /// </summary>
        public int TestResult { get; set; }
    }
}
