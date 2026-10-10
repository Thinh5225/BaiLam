package rmi_data1;
import RMI.DataService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class rmi_data1 {
    public static void main(String[] args) {
        try {
            // Thông tin sinh viên, mã câu hỏi và IP từ đề bài
            String studentCode = "B23DCCN790"; 
            String qCode = "nqR8NFsA"; // Mã câu hỏi đã cập nhật theo ảnh
            String serverIp = "36.50.135.242";
            int port = 1099;

            // Kết nối tới RMI Registry của server
            Registry registry = LocateRegistry.getRegistry(serverIp, port);

            // Tìm kiếm đối tượng DataService theo tên đã đăng ký là RMIDataService
            DataService service = (DataService) registry.lookup("RMIDataService");

            // a & b. Triệu gọi requestData để nhận mảng số nguyên
            Object receivedObj = service.requestData(studentCode, qCode);
            
            // Ép kiểu an toàn (đề phòng server trả về int[], Integer[] hoặc List)
            List<Integer> arrayData = new ArrayList<>();
            if (receivedObj instanceof int[]) {
                for (int x : (int[]) receivedObj) arrayData.add(x);
            } else if (receivedObj instanceof List) {
                arrayData.addAll((List<Integer>) receivedObj);
            } else if (receivedObj instanceof Integer[]) {
                for (Integer x : (Integer[]) receivedObj) arrayData.add(x);
            }

            // c. Tìm các "đỉnh cục bộ"
            List<Integer> peakPositions = new ArrayList<>();
            int n = arrayData.size();
            
            for (int i = 0; i < n; i++) {
                boolean isPeak = true;
                
                // Kiểm tra với phần tử liền kề bên trái (nếu có)
                if (i > 0 && arrayData.get(i) <= arrayData.get(i - 1)) {
                    isPeak = false;
                }
                
                // Kiểm tra với phần tử liền kề bên phải (nếu có)
                if (i < n - 1 && arrayData.get(i) <= arrayData.get(i + 1)) {
                    isPeak = false;
                }
                
                // Nếu là đỉnh cục bộ, thêm vị trí vào list (chỉ số bắt đầu từ 1 nên cộng thêm 1)
                if (isPeak) {
                    peakPositions.add(i + 1);
                }
            }

            System.out.println("Mảng ban đầu: " + arrayData);
            System.out.println("Vị trí đỉnh cục bộ gửi đi: " + peakPositions);

            // d. Triệu gọi submitData để gửi List<Integer> các vị trí đỉnh cục bộ
            service.submitData(studentCode, qCode, peakPositions);
            System.out.println("Đã gửi kết quả thành công!");

            // e. Kết thúc chương trình
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}