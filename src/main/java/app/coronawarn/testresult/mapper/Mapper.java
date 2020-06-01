package app.coronawarn.testresult.mapper;

public interface Mapper<S, D> {

  D map(S source);
}
