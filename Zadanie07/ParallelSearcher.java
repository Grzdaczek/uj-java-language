import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelSearcher implements ParallelSearcherInterface {
	Double counter;
	
	@Override
	public void set(HidingPlaceSupplierSupplier mainSupplier) {
		counter = 0.0;

		while(true) {
			HidingPlaceSupplier supplier = mainSupplier.get(counter);
			if (supplier == null) break;
			counter = 0.0;

			System.out.println(supplier.threads());

			List<SearcherThread> threads = Stream
				.generate(SearcherThread::new)
				.limit(supplier.threads())
				.collect(Collectors.toList());
			
			System.out.println(threads);

			threads.forEach(t -> {
				t.supplier = supplier;
				t.searcher = this;
			});

			threads.forEach(t -> t.start());
			
			threads.forEach(t -> {
				try {
					t.join();
				}
				catch (Exception e) {
					// (O.O)
				}
			});
		}
	}
}

class SearcherThread extends Thread {
	public HidingPlaceSupplier supplier = null;
	public ParallelSearcher searcher = null;

	public void run() {
		while (true) {
			HidingPlaceSupplier.HidingPlace place = supplier.get();
			if (place == null) break;

			if (place.isPresent()) {
				synchronized (searcher) {
					searcher.counter += place.openAndGetValue();
				}
			}
		}
	}
}