#See https://aka.ms/containerfastmode to understand how Visual Studio uses this Dockerfile to build your images for faster debugging.

FROM mcr.microsoft.com/dotnet/core/aspnet:3.1-buster-slim AS base
WORKDIR /app

FROM mcr.microsoft.com/dotnet/core/sdk:3.1-buster AS build
WORKDIR /src
COPY [".", "Covid19LabResultsService/"]
RUN dotnet restore "Covid19LabResultsService/Covid19LabResultsService.csproj"
COPY . .
WORKDIR "/src/Covid19LabResultsService"
RUN dotnet build "Covid19LabResultsService.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "Covid19LabResultsService.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
EXPOSE 8080
EXPOSE 8443
ENV ASPNETCORE_URLS= https://*:8443;http://*:8080
ENTRYPOINT ["dotnet", "Covid19LabResultsService.dll"]
