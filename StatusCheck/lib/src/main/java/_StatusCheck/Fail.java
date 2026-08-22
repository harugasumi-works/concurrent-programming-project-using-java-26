package _StatusCheck;

import java.time.Instant;

public record Fail(Instant timeStamp, int statusCode, String reason) implements Outcome{

}
