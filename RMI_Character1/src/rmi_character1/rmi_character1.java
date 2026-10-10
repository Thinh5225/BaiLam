package rmi_character1;

import RMI.CharacterService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.URI;

public class rmi_character1 {
    public static void main(String[] args) {
        try {
            // Thông tin sinh viên và mã câu hỏi lấy từ đề bài
            String studentCode = "B23DCCN790"; 
            String qCode = "SrNDra3z"; 
            String serverIp = "36.50.135.242"; 
            int port = 1099; // Cổng RMI mặc định

            // Kết nối tới RMI Registry của server
            Registry registry = LocateRegistry.getRegistry(serverIp, port);

            // Tìm kiếm đối tượng CharacterService theo tên đã đăng ký
            CharacterService service = (CharacterService) registry.lookup("RMICharacterService");

            // a. Triệu gọi requestCharacter để nhận chuỗi ngẫu nhiên
            String receivedString = service.requestCharacter(studentCode, qCode);
            System.out.println("Chuỗi gốc nhận được: " + receivedString);

            // b. Thực hiện mã hóa URL Encoding theo quy tắc URI path như đề bài yêu cầu
            String encodedString = new java.net.URI(null, null, receivedString, null).toASCIIString();
            System.out.println("Chuỗi sau khi mã hóa: " + encodedString);

            // c. Gửi chuỗi đã mã hóa trở lại server
            service.submitCharacter(studentCode, qCode, encodedString);
            System.out.println("Đã gửi kết quả thành công!");

            // d. Kết thúc chương trình
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}