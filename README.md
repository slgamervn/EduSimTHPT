# EduSim THPT

Ứng dụng Android mô phỏng tương tác phục vụ học sinh THPT lớp 10–12.

## Bản mẫu hiện tại
- Giao diện Android native bằng Kotlin và Jetpack Compose.
- Màn hình thư viện mô phỏng.
- Mô phỏng con lắc lò xo: điều chỉnh khối lượng, độ cứng, biên độ; chạy/tạm dừng/đặt lại; cập nhật li độ, vận tốc, gia tốc, chu kỳ và cơ năng.
- GitHub Actions tự động build APK khi push lên `main` hoặc chạy thủ công.

## Build
Yêu cầu JDK 17 và Android SDK phù hợp. Trong môi trường có Gradle:
```bash
gradle assembleDebug
```
APK đầu ra:
`app/build/outputs/apk/debug/app-debug.apk`

## Phát hành
Push tag phiên bản, ví dụ `v0.1.0`, để workflow tạo GitHub Release kèm APK.

## Roadmap
1. Hoàn thiện kiểm thử và biểu đồ x(t), v(t), a(t).
2. Thêm ném xiên và tam giác tương tác.
3. Thêm mô hình 3D, hóa học, sinh học, địa lý và lịch sử.
4. Thêm bài tập, tiến độ và dữ liệu offline.
