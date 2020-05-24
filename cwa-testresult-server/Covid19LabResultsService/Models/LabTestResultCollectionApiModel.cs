using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Covid19LabResultsService.Models
{
    public class LabTestResultCollectionApiModel
    {
        /// <summary>
        /// The test results
        /// </summary>
        [Required]
        public IEnumerable<LabTestResultApiModel> TestResults { get; set; }
    }
}
