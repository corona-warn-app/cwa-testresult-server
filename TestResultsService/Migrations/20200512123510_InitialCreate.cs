using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace CWA.TestResultsService.Migrations
{
    public partial class InitialCreate : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "test_results",
                columns: table => new
                {
                    id = table.Column<Guid>(nullable: false),
                    result_id = table.Column<string>(maxLength: 128, nullable: false),
                    result_date = table.Column<DateTime>(type: "datetime", nullable: false),
                    result = table.Column<int>(nullable: false),
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_test_results", x => x.id);
                });

            migrationBuilder.CreateIndex(
                name: "IX_test_results",
                table: "test_results",
                column: "result_id",
                unique: true);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "test_results");
        }
    }
}
