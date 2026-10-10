package rmi_byte2; // Thay đổi tên package này cho phù hợp với cấu trúc thư mục dự án của bạn

import RMI.ByteService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;
import java.util.Map;

public class rmi_byte2 {
    public static void main(String[] args) {
        try {
            // Thông tin sinh viên, mã câu hỏi và IP lấy từ đề bài trên hệ thống
            String studentCode = "B23DCCN790"; // Mã sinh viên của bạn
            String qCode = "q1SAHrn1";       // Mã câu hỏi cập nhật từ ảnh mới
            String serverIp = "36.50.135.242"; // IP Server
            int port = 1099; // Cổng RMI mặc định

            // Kết nối tới RMI Registry của server
            Registry registry = LocateRegistry.getRegistry(serverIp, port);

            // Tìm kiếm đối tượng ByteService theo tên đã đăng ký
            ByteService service = (ByteService) registry.lookup("RMIByteService");

            // a. Triệu gọi requestData để nhận mảng dữ liệu nhị phân
            byte[] receivedData = service.requestData(studentCode, qCode);
            
            // b. Tìm phần tử xuất hiện nhiều nhất
            // Sử dụng LinkedHashMap để đếm tần suất và giữ nguyên thứ tự xuất hiện đầu tiên của các phần tử
            LinkedHashMap<Byte, Integer> frequencyMap = new LinkedHashMap<>();
            for (byte b : receivedData) {
                frequencyMap.put(b, frequencyMap.getOrDefault(b, 0) + 1);
            }

            byte maxElement = receivedData[0];
            int maxFrequency = -1;

            // Duyệt qua map (theo thứ tự chèn) để tìm tần suất lớn nhất
            for (Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
                if (entry.getValue() > maxFrequency) {
                    maxFrequency = entry.getValue();
                    maxElement = entry.getKey();
                }
            }

            // Tạo mảng kết quả chứa [phần tử xuất hiện nhiều nhất, số lần xuất hiện]
            byte[] resultData = new byte[]{maxElement, (byte) maxFrequency};

            // c. Gửi mảng byte kết quả trở lại server
            service.submitData(studentCode, qCode, resultData);
            System.out.println("Đã gửi kết quả thành công!");

            // d. Kết thúc chương trình
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}