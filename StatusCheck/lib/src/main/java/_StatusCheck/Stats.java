package _StatusCheck;

public record Stats(int total, int successCount, int failureCount) {

static Stats summarize(ExecutionResult result) {
    int success = result.successes().size();
    int failure = result.failures().size();
    return new Stats(success + failure, success, failure);
}
}