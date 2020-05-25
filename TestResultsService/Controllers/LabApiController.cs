using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Threading.Tasks;
using CWA.TestResultsService.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;

namespace CWA.TestResultsService.Controllers
{
    [Route("api/v1/lab")]
    [ApiController]
    [Produces("application/json")]
    [ApiExplorerSettings(GroupName = "lab")]
    [Authorize]
    public class LabApiController : ControllerBase
    {
        private readonly ILogger<LabApiController> _logger;
        private readonly LabTestResultsDBContext _dbContext;


        public LabApiController(ILogger<LabApiController> logger, LabTestResultsDBContext context)
        {
            _logger = logger;
            _dbContext = context;
        }


        /// <summary>
        /// Create lab results
        /// </summary>
        /// <param name="labResults"></param>
        /// <response code="204">success</response>
        /// <response code="400">error (tbd)</response>            
        /// <response code="409">conflict (tbd)</response>            

        [HttpPost]
        [Route("results")]
        public async Task<ActionResult> CreateTestResults([FromBody] LabTestResultCollectionApiModel labResults)
        {
            if (labResults.TestResults != null)
            {
                _logger.LogInformation("adding {0} lab test results", labResults.TestResults.Count());
                foreach (var item in labResults.TestResults)
                {
                    AddUpdateTestResult(item);
                }
            
                int entries = await _dbContext.SaveChangesAsync();
                // TODO: return row count?
                _logger.LogInformation("{0} lab test results created", entries);
            }

            return NoContent();
        }

        private TestResultEntity AddUpdateTestResult(LabTestResultApiModel testResult)
        {
            if (String.IsNullOrWhiteSpace(testResult?.Id))
                return null;

            var entity = _dbContext.TestResults.SingleOrDefault(e => e.ResultId == testResult.Id);

            if(entity != null)
            {
                _dbContext.Update(entity);  // TODO: check, this should never happen !!???
            }
            else
            {
                entity = new TestResultEntity()
                {
                    Id = Guid.NewGuid(),
                    ResultId = testResult.Id
                };
                _dbContext.Add(entity);
            }
            entity.Result = testResult.Result;
            entity.ResultDate = DateTime.UtcNow;

            return entity;
        }
    }
}
