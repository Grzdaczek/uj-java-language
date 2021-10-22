import java.util.List;
import java.util.LinkedList;

public class Loops implements GeneralLoops {
	
	private List<Integer> lowerLimits = new LinkedList<Integer>();
	private List<Integer> upperLimits = new LinkedList<Integer>();

	private void loop(int index, List<Integer> acc, List<List<Integer>> result) {
		if(index == lowerLimits.size() || index == upperLimits.size()) {
			result.add(acc);
			return;
		}
		else {
			for(int i = lowerLimits.get(index); i <= upperLimits.get(index); i++) {
				List<Integer> newAcc = new LinkedList<Integer>(acc);
				newAcc.add(i);
				this.loop(index + 1, newAcc, result);
			}
		}
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
		this.lowerLimits = limits;
	}
	
	@Override
	public void setUpperLimits(List<Integer> limits) {
		this.upperLimits = limits;
	}
}
