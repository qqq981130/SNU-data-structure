import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 *
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를
 * 유지하는 데이터베이스이다.
 */
public class MovieDB {
	private Node<MovieList> head= new Node<>(null);
	private int numItems = 0; //장르의 수

	public int getNumItems() {
		return numItems;
	}

	public void insert(String genreName, String movieName) {
		Node<MovieList> prev = head;

		//아직 장르가 하나도 추가되지 않았을 경우
		if (this.numItems == 0) { 
			MovieList newMovieList = new MovieList(genreName);
			Node<MovieList> next = new Node<>(newMovieList); //장르명으로 된 인스턴스 변수를 가지는 MovieList를 item으로 갖는 Node를 만듦.
			this.numItems++;

			prev.setNext(next);
			next.getItem().add(movieName);
			return;
		}

		//이미 장르가 들어있다면?
		for (;prev.getNext() != null; prev = prev.getNext()) {
			String nextGenreName = prev.getNext().getItem().getGenreName();

			if (genreName.compareTo(nextGenreName) == 0) {
				prev.getNext().getItem().add(movieName); //이미 해당 장르가 있다면 거기에 add해주면 됨.
				return;
			}
			else if (genreName.compareTo(nextGenreName) < 0) { //다음 장르보다 먼저 들어와야 한다면?
				Node<MovieList> next = new Node<>(new MovieList(genreName));
				this.numItems++;

				next.getItem().add(movieName);
				next.setNext(prev.getNext());
				prev.setNext(next);
				return;
			}
		}


		//장르가 마지막에 추가되어야 한다면?
		prev.setNext(new Node<MovieList>(new MovieList(genreName)));
		this.numItems++;
		prev.getNext().getItem().add(movieName);

	}

	public void delete(String targetGenre, String targetMovie) {
		Node<MovieList> prev = head;
		if (numItems == 0) { //아무것도 저장 안돼있으면 아무것도 안 해야함.
			return;
		}
		else {
			for (int i = 0; i<numItems; i++) {
				Node<MovieList> curr = prev.getNext();
				MovieList currItem = curr.getItem();

				if (targetGenre.compareTo(currItem.getGenreName()) == 0) { //장르 일치
					currItem.removeMovie(targetMovie); //해당 장르에서 targetMovie 삭제

					if (currItem.getNumItems() == 0) { //이 결과 해당 장르의 모든 영화가 삭제되면
						prev.setNext(curr.getNext());
						numItems--;
					}
					return;
				}
				prev = prev.getNext(); //없었으면 다음 장르로 넘어감.
			}
		//루프를 빠져나왔다면 한 바퀴 다 돌았는데 일치하는 장르 없었다는 뜻. 아무것도 안 해도 됨.
		}
		

	}

	public MyLinkedList<MovieDBItem> search(String term) {
		if (numItems == 0) {
			return new MyLinkedList<MovieDBItem>();
		}

		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>(); 
		Node<MovieList> genreCurr = this.head.getNext();
		Node<MovieDBItem> resultsPrevNode = results.head;

		for (;genreCurr!=null; genreCurr = genreCurr.getNext()) {
			MovieList targetMovieList = genreCurr.getItem();
			Node<String> targetMovieListCurr = targetMovieList.getHead().getNext();
			for (; targetMovieListCurr != null; targetMovieListCurr = targetMovieListCurr.getNext()) {
				if ((targetMovieListCurr.getItem()).contains(term)) {
					Node<MovieDBItem> newNode = new Node<>(new MovieDBItem(targetMovieList.getGenreName(), targetMovieListCurr.getItem()));
					resultsPrevNode.setNext(newNode);
					results.numItems++;
					resultsPrevNode = resultsPrevNode.getNext();
				}
			}
		}
		return results;
	}

	public MyLinkedList<MovieDBItem> items() {

		if (numItems == 0) {
			return new MyLinkedList<MovieDBItem>();
		}

		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		Node<MovieList> genreCurr = this.head.getNext();
		Node<MovieDBItem> resultsPrevNode = results.head;

		for (;genreCurr!=null; genreCurr = genreCurr.getNext()) {
			MovieList targetMovieList = genreCurr.getItem();
			Node<String> targetMovieListCurr = targetMovieList.getHead().getNext();
			for (;targetMovieListCurr != null; targetMovieListCurr = targetMovieListCurr.getNext()) {
				Node<MovieDBItem> newNode = new Node<>(new MovieDBItem(targetMovieList.getGenreName(), targetMovieListCurr.getItem()));
				resultsPrevNode.setNext(newNode);
				results.numItems++;
				resultsPrevNode = resultsPrevNode.getNext();
			}
		}
		return results;
	}
}


class MovieList implements ListInterface<String> { //장르 내부에 존재하는, 해당 장르에 속하는 영화의 목록
	private final String genreName;
	private final Node<String> head;
	int numItems=0; //여기 들어있는 영화의 수

	public MovieList(String genreName) {
		head = new Node<String>(null);
		this.genreName = genreName;
	}

	public Node<String> getHead() { return this.head;}

	public String getGenreName() {
		return this.genreName;
	}

	public int getNumItems() {return this.numItems;}

	public void removeMovie(String target) {
		Node<String> prev = head;

		while(prev.getNext() != null) {
			Node<String> curr = prev.getNext();
			if (target.compareTo(curr.getItem())==0) {
				prev.setNext(curr.getNext());
				numItems--;
				return;
			}
			else {
				prev = prev.getNext();
			}
		}
		return; //이 루프를 빠져나왔다면 삭제를 위해 찾는 영화가 없는것. 아무것도 안하고 종료.
	}

	@Override
	public Iterator<String> iterator() {
		return new MovieListIterator(this);
	}

	@Override
	public boolean isEmpty() {
		return (numItems == 0);
	}

	@Override
	public int size() {
		return numItems;
	}

	@Override
	public void add(String item) { //sort해서 add해라.
		Node<String> prev = head;

		if (numItems == 0) {
			prev.setNext(new Node<>(item));
			numItems++;
			return;
		}

		else {
			for (; prev.getNext()!=null; prev = prev.getNext()) {
				if (item.compareTo(prev.getNext().getItem()) == 0) {
					return;
				}
				else if (item.compareTo(prev.getNext().getItem()) < 0) { 
					Node<String> newNode = new Node<>(item, prev.getNext());
					prev.setNext(newNode);
					numItems++;
					return;
				}
			}
			//만약 여기까지 다 통과했으면 겹치는 것도 없고 item이 가장 뒤에 추가되어야 함.
			Node<String> newNode = new Node<>(item);
			prev.setNext(newNode);
			numItems++;
		}
	}

	@Override
	public String first() {
		return head.getNext().getItem();
	}

	@Override
	public void removeAll() {
		head.setNext(null);
	}
}


class MovieListIterator implements Iterator<String> {
	private MovieList list;
	private Node<String> curr;
	private Node<String> prev;

	public MovieListIterator(MovieList list) {
		this.list = list;
		this.curr = list.getHead();
		this.prev = null;
	}

	public boolean hasNext() {
		return (this.curr.getNext() != null);
	}

	public String next() {
		if (!hasNext())
			throw new NoSuchElementException();

		prev = curr;
		curr = curr.getNext();
		return curr.getItem();
	}

	public void remove() {
		if (prev == null)
			throw new IllegalStateException("next() should be called first");
		if (curr == null)
			throw new NoSuchElementException();

		prev.removeNext();
		list.numItems -= 1;
		curr = prev;
		prev = null;}
}
