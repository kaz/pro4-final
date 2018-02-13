package pro4.test;

import junit.framework.TestCase;
import pro4.ticket.Ticket;

public class TicketTest extends TestCase {

	// 正しく予約ができる
	public void testReserve() {
		Ticket t = Ticket.reserve(null, null, null, 0, 0);
		assertEquals(1, Ticket.getAll().count());
		t.cancel();
	}

	// 予約はキャンセルできる
	public void testCancel() {
		Ticket t = Ticket.reserve(null, null, null, 0, 0);
		t.cancel();
		assertEquals(0, Ticket.getAll().count());
	}

	// 予約は名前で検索できる
	public void testGetByUser() {
		Ticket t1 = Ticket.reserve("a", null, null, 0, 0);
		Ticket t2 = Ticket.reserve("b", null, null, 0, 0);
		Ticket t3 = Ticket.reserve("a", null, null, 0, 0);
		assertEquals(2, Ticket.getByUser("a").count());
		assertEquals(1, Ticket.getByUser("b").count());
		t1.cancel();
		t2.cancel();
		t3.cancel();
	}
	
	// 予約はhashCodeで一意に識別できる
	public void testGetByCode() {
		Ticket t = Ticket.reserve("a", null, null, 1, 2);
		assertEquals(t, Ticket.getByCode(t.hashCode()));
		t.cancel();
	}
	
}
