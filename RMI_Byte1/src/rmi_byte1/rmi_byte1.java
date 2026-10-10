package rmi_byte1;
import RMI.ByteService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;
import java.util.Map;

public class rmi_byte1 {
    public static void main(String[] args) {
        try {
            // Thông tin sinh viên, mã câu hỏi và IP lấy từ đề bài trên hệ thống
            String studentCode = "B23DCCN790"; 
            String qCode = "2XY2gIa9"; 
            String serverIp = "36.50.135.242"; 
            int port = 1099; // Cổng RMI mặc định

            // Kết nối tới RMI Registry của server
            Registry registry = LocateRegistry.getRegistry(serverIp, port);

            // Tìm kiếm đối tượng ByteService theo tên đã đăng ký
            ByteService service = (ByteService) registry.lookup("RMIByteService");

            // a. Triệu gọi requestData để nhận mảng dữ liệu nhị phân
            byte[] receivedData = service.requestData(studentCode, qCode);
            
            // b. Tìm phần tử có số lần xuất hiện ít nhất
            // Sử dụng LinkedHashMap để đếm tần suất và giữ nguyên thứ tự xuất hiện đầu tiên của các phần tử
            LinkedHashMap<Byte, Integer> frequencyMap = new LinkedHashMap<>();
            for (byte b : receivedData) {
                frequencyMap.put(b, frequencyMap.getOrDefault(b, 0) + 1);
            }

            byte minElement = receivedData[0];
            int minFrequency = Integer.MAX_VALUE;

            // Duyệt qua map (theo thứ tự chèn) để tìm tần suất nhỏ nhất
            for (Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
                if (entry.getValue() < minFrequency) {
                    minFrequency = entry.getValue();
                    minElement = entry.getKey();
                }
            }

            // Tạo mảng kết quả chứa [phần tử xuất hiện ít nhất, số lần xuất hiện]
            byte[] resultData = new byte[]{minElement, (byte) minFrequency};

            // c. Gửi mảng byte kết quả trở lại server
            service.submitData(studentCode, qCode, resultData);
            System.out.println("Đã gửi kết quả thành công!");

            // d. Kết thúc chương trình
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}