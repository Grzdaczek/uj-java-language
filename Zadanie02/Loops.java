import java.util.List;
import java.util.LinkedList;

public class Loops implements GeneralLoops {
	
	private List<Integer> lowerLimits;
	private List<Integer> upperLimits;
	private int size;

	public Loops() {
		lowerLimits = new LinkedList<Integer>();
		upperLimits = new LinkedList<Integer>();
		lowerLimits.add(0);
		upperLimits.add(0);
		size = 1;
	}
	
	@Override
	public List<List<Integer>> getResult() {
		List<List<Integer>> result = new LinkedList<List<Integer>>(); 
		List<Integer> acc = new LinkedList<Integer>();
		loop(0, acc, result);
		return result;
	}

	@Override
	public void setLowerLimits(List<Integer> limits) {
		lowerLimits = limits;
		size = Math.max(lowerLimits.size(), upperLimits.size());
	}
	
	@Override
	public void setUpperLimits(List<Integer> limits) {
		upperLimits = limits;
		size = Math.max(lowerLimits.size(), upperLimits.size());
	}

	private void loop(int index, List<Integer> acc, List<List<Integer>> result) {
		if(index == size) {
			result.add(acc);
			return;
		}
		else {
			int lower = lowerLimits.size() >= size ? lowerLimits.get(index) : 0;
			int upper = upperLimits.size() >= size ? upperLimits.get(index) : 0;
			for(int i = lower; i <= upper; i++) {
				List<Integer> newAcc = new LinkedList<Integer>(acc);
				newAcc.add(i);
				loop(index + 1, newAcc, result);
			}
		}
	}

}
