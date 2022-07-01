import java.io.*;
import java.util.*;

public class SortingTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
					}
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////

	private static int[] DoBubbleSort(int[] value)
	{
		boolean sortedFlag = true;

		for (int j = value.length -1; j>0; j--) {
			for (int i = 0; i<j; i++) {
				if (value[i] > value[i+1]) {
					int temp = value[i+1]; value[i+1] = value[i]; value[i] = temp;
					sortedFlag = false;
				}
			}
			if (sortedFlag) {
				return value;
			}
		}
		return value;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////

	private static int[] DoInsertionSort(int[] value)
	{
		for (int i = 1; i<value.length; i++) {
			for (int j = i; j>0; j--) {
				if (value[j-1]>value[j]) {
					int temp = value[j]; value[j] = value[j-1]; value[j-1] = temp;
				}
				else {
					break;
				}
			}
		}

		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////

	private static int[] DoHeapSort(int[] value)
	{
		value = buildHeap(value);
		heapSort(value, value.length);
		return value;
	}

	private static int[] buildHeap(int[] value) {
		if (value.length >= 2) {
			int i = (value.length - 2) / 2; //처음으로 리프 노드가 아닌 노드의 index

			for (; i >= 0; i--) {
				percolateDown(value, i, value.length);
			}
		}
		return value;
	}

	 private static void percolateDown(int[] value, int i, int n) { //i:percolateDown 하고자 하는 노드의 index
		 															//n:정렬할 힙의 크기
		 while(i<=n-1) {
			 int child = 2*i+1;
			 int rightChild = 2*i+2;

			 if (child > n-1) {
				 break;
			 }

			 if (rightChild<=n-1) {
				 if (value[child] < value[rightChild]) {
					 child = rightChild;
				 }
			 }

			 if (value[i]<value[child]) {
				 int temp = value[child]; value[child] = value[i]; value[i] = temp; //swap
				 i = child;
			 }
			 else {
				 break;
			 }
		 }
	 }

	 private static void heapSort(int[] value, int n) { //n:정렬할 힙의 크기

		 for (int i = n; i>0; i--) {
			 int temp = value[0]; value[0] = value[i-1]; value[i-1] = temp; //swap
			 percolateDown(value, 0, i-1);
		 }
	 }

	////////////////////////////////////////////////////////////////////////////////////////////////////

	private static int[] DoMergeSort(int[] value)
	{
		int[] value2 = new int[value.length];

		for (int i = 0; i<value.length; i++) {
			value2[i] = value[i];
		}

		mergeSort(value, value2, 0, value.length - 1);
		return value;

	}

	private static void mergeSort(int[] value, int[] value2, int p, int r) {//p:시작 index. r:마지막 index
		if (r<=p) {
			return;
		}
		else {
			int mid = (p+r)/2;
			mergeSort(value2, value, p, mid);
			mergeSort(value2, value, mid+1, r);
			merge(p, mid, r, value2, value);
		}
	}

	private static void merge(int p, int mid, int r, int[] value, int[] value2) {
		int i=p;
		int j=mid+1;
		int k=p;

		while (i<=mid && j<=r) {
			if (value[i]<=value[j]) {
				value2[k++] = value[i++];
			}
			else {
				value2[k++] = value[j++];
			}
		}
		while (i<=mid) {
			value2[k++] = value[i++];
		}
		while (j<=r) {
			value2[k++] = value[j++];
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////

	private static int[] DoQuickSort(int[] value)
	{
		quickSort(value,0, value.length - 1);
		return value;
	}

	private static void quickSort(int[] value, int p, int r) {
		if (r<=p) {
			return;
		}
		else {
			int q = partition(value, p, r);
			quickSort(value, p, q-1);
			quickSort(value, q+1, r);
		}
	}

	private static int partition(int[] value, int p, int r) { //p:시작 index. r:마지막 index
		Random rand = new Random();
		int randomIndex = rand.nextInt(r-p) + p;

		int temp = value[r]; value[r] = value[randomIndex]; value[randomIndex] = temp;

		int std = value[r];
		int i = p-1;
		for (int j =p; j<r; j++) {
			if (value[j] < std || ((value[j]==std) &&(Math.random() < 0.5))) { //동일한 수가 많다면 한쪽으로 partition이 치우치지 않도록 랜덤함수 사용
				temp = value[++i]; value[i] = value[j]; value[j] = temp;
			}
		}
		temp = value[++i]; value[i] = value[r]; value[r] = temp;
		return i;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////

	private static int[] DoRadixSort(int[] value)
	{
		int[] cnt = new int[19], start = new int[19];
		int[] B = new int[value.length]; //value를 옮겨담아줄 배열

		int max = value[0];

		for (int i =0; i<value.length; i++) {
			int tmp = value[i] < 0 ? -value[i] : value[i]; //절댓값으로 만들어준다
			max = Math.max(max, tmp);
		}

		int numDigits = (int)Math.log10(max) + 1; //최대자릿수를 계산한다.

		for (int digit = 1; digit <= numDigits; digit++) {
			for (int d = 0; d<19; d++) {
				cnt[d] = 0; //일단 cnt 배열 초기화
			}

			for (int i = 0; i<value.length; i++) {
				cnt[(int)(value[i]/Math.pow(10, digit-1)%10) + 9]++; //예를들어, cnt[0]: digit자리의 수가 -9인 숫자 수
			}

			start[0] = 0;

			for (int d = 1; d<19; d++) {
				start[d] = start[d-1] + cnt[d-1];
			}

			for (int i = 0; i<value.length; i++) {
				B[start[(int)(value[i]/Math.pow(10, digit-1)%10)+9]++] = value[i];
			}

			for (int i = 0; i<value.length; i++) {
				value[i] = B[i];
			}
		}

		return value;
	}
}
