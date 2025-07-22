import static org.junit.Assert.*;
import org.junit.Test;


public class TestArrayDequeGold {
    @Test
    public void test() {
        ArrayDequeSolution<Integer> expected = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> real = new StudentArrayDeque<>();
        String message = "";

        for (int i = 0; i < 500; i++) {
            Double op = StdRandom.uniform(0.0, 4.0);
            Integer num = StdRandom.uniform(20);
            if (op < 1.0) {
                expected.addFirst(num);
                real.addFirst(num);
                message += "addFirst(" + num + ")\n";
                assertEquals(message, expected.size(), real.size());
            }
            else if (op < 2.0) {
                expected.addLast(num);
                real.addLast(num);
                message += "addLast(" + num + ")\n";
                assertEquals(message, expected.size(), real.size());
            }
            else if (op < 3.0) {
                if (expected.isEmpty() || real.isEmpty()) {
                    continue;
                }
                else {
                    Integer ex = expected.removeFirst();
                    Integer re = real.removeFirst();
                    message += "removeFirst()\n";
                    assertEquals(message, ex, re);
                }
            }
            else {
                if (expected.isEmpty() || real.isEmpty()) {
                    continue;
                }
                else {
                    Integer ex = expected.removeLast();
                    Integer re = real.removeLast();
                    message += "removeLast()\n";
                    assertEquals(message, ex, re);
                }
            }
        }
    }
}
