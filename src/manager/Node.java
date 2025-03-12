package manager;

public class Node<E> {
    // Данные
    public E data;
   // Ссылка на следующий элемент
   public Node<E> next;
   // Ссылка на предыдущий элемент
    public Node<E> prev;

    public Node(Node<E> prev, E data, Node<E> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

}
