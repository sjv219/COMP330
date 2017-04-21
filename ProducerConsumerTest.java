/*ProducerConsumerTest.java: Instantiates CubbyHole, Producer and Consumer
classes and starts Producer and Consumer threads.*/
public class ProducerConsumerTest {
	public static void main(String[] args) {
		CubbyHole c = new CubbyHole();
		Producer p1 = new Producer(c, 1);
		Consumer c1 = new Consumer(c, 1);

		p1.start();
		c1.start(); 
	}
}

/*CubbyHole class contains the shared contents which is stored by the Producer  Object and consumed by the Consumer object. In order to allow the Consumer
object to consume the contents only once as soon as the Producer object stores
the contents, the synchronized keyword is placed in the put and get methods */ 
class CubbyHole {
	private int contents; //shared data
	private boolean available = false;

	public synchronized int get() {
		while (available == false) {
			try {
			  wait(); /* Consumer enters a wait state until notified				   by producer */
			}
			catch (InterruptedException e) { }
		}
		available = false;
		notifyAll(); /* Consumer notifies Producer the it can store new 				content */
		return contents;
	}

	//method used by consumer to access (stores) the shared data
	public synchronized void put(int value) {
		while (available == true) {
			try {
			   wait(); /* Producer who wants to store contents enter					a wait state until notified by the
					Consumer */
			}
			catch (InterruptedException e) { }
		}
		contents = value;
		available = true;
		notifyAll(); /* Producer notifies Consumer to come out of the 
				wait state and consume the contents */
	}
}


/* Consumer is class of Thread that consumes and prints all numbers from the 
cubbyhole object as soon as the producer stores a number into the same
cubbyhole object. */
class Consumer extends Thread {
	private CubbyHole cubbyhole; //shared data
	private int number;

	public Consumer (CubbyHole c, int number) {
		cubbyhole = c;
		this.number = number;
	}

	public void run () {
		int value = 0;
		for (int i = 0; i < 10; i++) {
			value = cubbyhole.get(); //Assessing shared data
			System.out.println("Consumer #" + this.number
						+ " got: " + value);
		}
	}
}

/* The Producer Class is a class of Thread that generates and stores 
incrementing numbers into a CubbyHole object with random pauses in between
each increment. Also, each number generated by The Producer object is printed
to the screen. */
class Producer extends Thread {
	private CubbyHole cubbyhole; //shared data
	private int number;

	public Producer (CubbyHole c, int number) {
		cubbyhole = c;
		this.number = number;
	}

	public void run () {
		for (int i = 0; i < 10; i++) {
			cubbyhole.put(i); //Assessing shared data
			System.out.println("Producer #" + this.number 
						+ " put: " + i);

			try {
				sleep((int)(Math.random() * 100));
			}
			catch (InterruptedException e) { }
		}
	}
}


