package ch.fhnw.apm.docfinder;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@State(Scope.Benchmark)
public class DocFinderBenchmarks {

    @Param({"woman friend cat", "king alice", "club penguin", "lego"})
    private String SEARCH_TEXT;

    private DocFinder finder;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DocFinderBenchmarks.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }

    @Setup
    public void setup() {
        var booksDir = Path.of("perf-tests/books").toAbsolutePath();
        if (!Files.isDirectory(booksDir)) {
            System.err.println("Directory perf-tests/books not found. " +
                    "Make sure to run this program in the doc-finder directory.");
            System.exit(1);
        }

        finder = new DocFinder(booksDir);
    }

    @Warmup(iterations = 1)
    @Measurement(iterations = 3, time = 5)
    @BenchmarkMode(Mode.SampleTime)
    @Benchmark
    public List<Result> findDocs() throws IOException, InterruptedException {
        return finder.findDocs(SEARCH_TEXT);
    }
}
