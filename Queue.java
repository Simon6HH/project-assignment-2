import java.util.Arrays;
import java.util.LinkedList;

public class Queue<T> {
    private LinkedList<Integer> array; /** global variable **/

    public Queue() { /** new queue thats a linked list **/
        array = new LinkedList<>();
    }

    public void Enqueue(int item) { /** enqueues the queue by adding the item **/
        array.add(item);
    }

    public int Dequeue() { /** dequeues the queue where it removes the first(previous) **/
        int T = array.getFirst();
        array.removeFirst();
        return T;
    }

    public boolean Empty() { /** empty function to check if array is empty **/
        return array.isEmpty();
    }

    public int size() { /** might not be used **/
        return array.size();
    }

    public void print() { /** might not be used **/
        System.out.println(Arrays.toString(array.toArray()));
    }
}