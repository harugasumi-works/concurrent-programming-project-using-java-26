package _StatusCheck;

import java.time.Instant;

public record Success(Instant timeStamp, int statusCode, long latency) implements Outcome{

}
