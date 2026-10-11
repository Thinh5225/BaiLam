package rmi_object;

import RMI.ObjectService;
import RMI.TicketSla;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_object {
    public static void main(String[] args) {
        try {
            // Thông tin bài tập
            String studentCode = "B23DCCN790"; // Mã sinh viên của bạn
            String qCode = "p8IJFurz";       // Mã câu hỏi theo đề bài
            String serverIp = "36.50.135.242";
            int port = 1099;

            // Kết nối Registry
            Registry registry = LocateRegistry.getRegistry(serverIp, port);
            ObjectService service = (ObjectService) registry.lookup("RMIObjectService");

            // a & b. Triệu gọi requestObject để nhận đối tượng TicketSla
            TicketSla ticket = (TicketSla) service.requestObject(studentCode, qCode);
            
            if (ticket == null) {
                System.out.println("Lỗi: Nhận về null từ server. Vui lòng thử reset lại bài trên web!");
                return;
            }

            System.out.println("Nhận Ticket: Priority=" + ticket.getPriority() + ", OpenedHoursAgo=" + ticket.getOpenedHoursAgo());

            // c. Thiết lập breached
            String priority = ticket.getPriority();
            int hours = ticket.getOpenedHoursAgo();
            boolean isBreached = false;

            if (priority.equals("CRITICAL") && hours > 2) {
                isBreached = true;
            } else if (priority.equals("HIGH") && hours > 8) {
                isBreached = true;
            } else if (priority.equals("MEDIUM") && hours > 24) {
                isBreached = true;
            } else if (priority.equals("LOW") && hours > 72) {
                isBreached = true;
            }
            
            ticket.setBreached(isBreached);

            // Thiết lập action
            if (!isBreached) {
                ticket.setAction("MONITOR");
            } else if (priority.equals("CRITICAL") || hours > 96) {
                ticket.setAction("ESCALATE_L2");
            } else {
                ticket.setAction("ESCALATE_L1");
            }

            System.out.println("Cập nhật: Breached=" + ticket.isBreached() + ", Action=" + ticket.getAction());

            // d. Gửi lại đối tượng đã cập nhật
            service.submitObject(studentCode, qCode, ticket);
            System.out.println("Đã gửi kết quả thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}