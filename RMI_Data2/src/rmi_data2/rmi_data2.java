package rmi_data2;

import RMI.DataService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class rmi_data2 {
    public static void main(String[] args) {
        try {
            // Thông tin sinh viên, mã câu hỏi lấy từ đề bài
            String studentCode = "B23DCCN790"; 
            String qCode = "NxKVJ9R1"; // Mã câu hỏi hiện tại
            String serverIp = "36.50.135.242"; 
            int port = 1099;

            // Kết nối tới RMI Registry của server
            Registry registry = LocateRegistry.getRegistry(serverIp, port);

            // Tìm kiếm đối tượng DataService
            DataService service = (DataService) registry.lookup("RMIDataService");

            // a & b. Nhận chuỗi CSV các số thực từ server
            String csvData = (String) service.requestData(studentCode, qCode);
            System.out.println("Chuỗi nhận được: " + csvData);

            // Tách chuỗi và chuyển sang danh sách các số thực (Double)
            String[] parts = csvData.split(",");
            List<Double> numbers = new ArrayList<>();
            double sum = 0;
            
            for (String part : parts) {
                double value = Double.parseDouble(part.trim());
                numbers.add(value);
                sum += value;
            }

            int n = numbers.size();

            // c. Tính toán các giá trị thống kê
            // Tính giá trị trung bình (average)
            double average = sum / n;

            // Tính độ lệch chuẩn (standard deviation) trên toàn bộ dãy
            double sumOfSquaredDifferences = 0;
            for (double num : numbers) {
                sumOfSquaredDifferences += Math.pow(num - average, 2);
            }
            double stddev = Math.sqrt(sumOfSquaredDifferences / n);

            // Tính phân vị 95 (p95)
            Collections.sort(numbers); // Sắp xếp tăng dần
            int p95Index = (int) Math.ceil(n * 0.95) - 1; // Công thức từ đề bài
            double p95 = numbers.get(p95Index);

            // d. Tạo chuỗi kết quả theo đúng định dạng, làm tròn 2 chữ số thập phân
            // Lưu ý: Sử dụng Locale.US để đảm bảo dấu thập phân là dấu chấm (.) thay vì dấu phẩy (,)
            String result = String.format(Locale.US, "average=%.2f;stddev=%.2f;p95=%.2f", average, stddev, p95);
            System.out.println("Chuỗi kết quả gửi đi: " + result);

            // Gửi kết quả lên server
            service.submitData(studentCode, qCode, result);
            System.out.println("Đã gửi kết quả thành công!");

            // e. Kết thúc chương trình
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}