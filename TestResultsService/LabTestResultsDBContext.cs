using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.Extensions.Configuration;

namespace CWA.TestResultsService
{
    public partial class LabTestResultsDBContext : DbContext
    {
        public LabTestResultsDBContext()
        {
        }

        public LabTestResultsDBContext(DbContextOptions<LabTestResultsDBContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Models.TestResultEntity> TestResults { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Models.TestResultEntity>(entity =>
            {
                entity.ToTable("test_results");

                entity.HasIndex(e => e.ResultId)
                    .HasName("IX_test_results")
                    .IsUnique();

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .ValueGeneratedNever();

                entity.Property(e => e.Result).HasColumnName("result");

                entity.Property(e => e.ResultDate)
                    .HasColumnName("result_date")
                    .HasColumnType("timestamp");

                entity.Property(e => e.ResultId)
                    .IsRequired()
                    .HasColumnName("result_id")
                    .HasMaxLength(128);
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
