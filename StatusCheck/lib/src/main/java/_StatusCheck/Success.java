package _StatusCheck;

public record Success(int statusCode, long latency) implements Outcome{

}
