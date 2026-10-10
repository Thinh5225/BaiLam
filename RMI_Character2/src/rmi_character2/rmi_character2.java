package rmi_character2;

import RMI.CharacterService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class rmi_character2 {
    public static void main(String[] args) {
        try {
            // Thông tin sinh viên, mã câu hỏi và IP từ đề bài
            String studentCode = "B23DCCN790"; 
            String qCode = "2n59cxdB"; 
            String serverIp = "36.50.135.242"; 
            int port = 1099; // Cổng RMI mặc định

            // Kết nối tới RMI Registry của server
            Registry registry = LocateRegistry.getRegistry(serverIp, port);

            // Tìm kiếm đối tượng CharacterService theo tên RMICharacterService
            CharacterService service = (CharacterService) registry.lookup("RMICharacterService");

            // a. Triệu gọi requestCharacter để nhận số thập phân dạng chuỗi
            String receivedString = service.requestCharacter(studentCode, qCode);
            System.out.println("Số thập phân nhận được: " + receivedString);
            
            // THÊM ĐOẠN KIỂM TRA NÀY ĐỂ TRÁNH LỖI NULLPOINTEREXCEPTION
            if (receivedString == null) {
                System.out.println("Lỗi: Server trả về giá trị null. Hãy kiểm tra lại phiên làm việc hoặc trạng thái bài trên hệ thống!");
                return;
            }

            // b. Chuyển đổi số thập phân thành chuỗi số La Mã
            int number = Integer.parseInt(receivedString.trim());
            String romanNumeral = intToRoman(number);
            System.out.println("Chuỗi số La Mã sau khi chuyển đổi: " + romanNumeral);

            // c. Triệu gọi submitCharacter để gửi chuỗi số La Mã trở lại server
            service.submitCharacter(studentCode, qCode, romanNumeral);
            System.out.println("Đã gửi kết quả thành công!");

            // d. Kết thúc chương trình
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm hỗ trợ chuyển đổi số nguyên sang số La Mã
    public static String intToRoman(int num) {
        // Khai báo các mốc giá trị La Mã cơ bản và các trường hợp đặc biệt (như 4, 9, 40, 90...)
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romanLetters = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        
        StringBuilder roman = new StringBuilder();
        
        // Duyệt qua từng mốc giá trị từ lớn đến bé
        for (int i = 0; i < values.length; i++) {
            // Chừng nào số hiện tại còn lớn hơn hoặc bằng mốc giá trị
            while (num >= values[i]) {
                num -= values[i];                 // Trừ đi mốc giá trị đó
                roman.append(romanLetters[i]);    // Thêm ký tự La Mã tương ứng vào kết quả
            }
        }
        return roman.toString();
    }
}