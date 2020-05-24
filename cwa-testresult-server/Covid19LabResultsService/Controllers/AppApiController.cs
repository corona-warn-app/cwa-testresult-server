using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using Covid19LabResultsService.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore.Metadata.Internal;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;

namespace Covid19LabResultsService.Controllers
{
    [Route("api/v1/app")]
    [ApiController]
    [Produces("application/json")]
    [ApiExplorerSettings(GroupName = "app")]
    //[Authorize]
    public class AppApiController : ControllerBase
    {
        private readonly ILogger<AppApiController> _logger;
        private readonly LabTestResultsDBContext _dbContext;
        private readonly IConfiguration _configuration;

        public AppApiController(ILogger<AppApiController> logger, IConfiguration config, LabTestResultsDBContext context)
        {
            _logger = logger;
            _dbContext = context;
            _configuration = config;
        }

        /// <summary>
        /// Query the the test result for given id
        /// </summary>
        /// <param name="request"></param>
        /// <returns>The test result</returns>
        [HttpPost]
        [Route("result")]
        public ActionResult<TestResultResponseApiModel> QueryTestResult([FromBody] TestResultRequestApiModel request)
        {
            try
            {
                var response = new TestResultResponseApiModel();
                string resultId = request.Id.ToLowerInvariant(); // id is requried , check ModelState.IsValid?

                var re = _dbContext.TestResults.SingleOrDefault(e => e.ResultId == resultId);

                _logger.LogInformation("test result {0}available.", re == null ? "not " : String.Empty);

                // map database test result to app test result
                // Database:   0            1: negative,    2: positive, 3: invalid
                // App:        0: Pending,  1: Negative     2: Positive, 3: Invalid
                switch (re?.Result)
                {
                    default:
                    case 0:
                    case null:
                        response.TestResult = 0;
                        break;
                    case 1:
                    case 2:
                    case 3:
                        response.TestResult = re.Result;
                        break;
                }

                return Ok(response);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.ToString());
            }
            return StatusCode(500);
        }
    }
}