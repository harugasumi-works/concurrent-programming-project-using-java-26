package _StatusCheck;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

public class CustomJoin implements StructuredTaskScope.Joiner<ScanResult, ExecutionResult> {
	
	private final ConcurrentLinkedQueue<ScanResult> successes = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<ScanResult> failures = new ConcurrentLinkedQueue<>();
	
    @SuppressWarnings("preview")
	@Override 
    public boolean onComplete(Subtask<ScanResult> subtask) { //
    	ScanResult result = subtask.get();
        if (result instanceof ScanResult(_, Success(_, _, _))) {
            successes.add(subtask.get());
        } else failures.add(subtask.get());
        return false;
    }
	
    @Override
    public ExecutionResult result() { //
        return new ExecutionResult(List.copyOf(successes), List.copyOf(failures));
    }
}
