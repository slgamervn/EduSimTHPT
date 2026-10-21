package vn.edusim.thpt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.*

private val Ink = androidx.compose.ui.graphics.Color(0xFF122033)
private val Teal = androidx.compose.ui.graphics.Color(0xFF14B8A6)
private val Panel = androidx.compose.ui.graphics.Color(0xFF1C2B40)
private val Muted = androidx.compose.ui.graphics.Color(0xFF9FB0C5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EduSimApp() }
    }
}

@Composable
fun EduSimApp() {
    var page by remember { mutableStateOf("home") }
    MaterialTheme(colorScheme = darkColorScheme(
        primary = Teal, background = Ink, surface = Panel,
        onBackground = androidx.compose.ui.graphics.Color.White,
        onSurface = androidx.compose.ui.graphics.Color.White
    )) {
        Surface(Modifier.fillMaxSize(), color = Ink) {
            AnimatedContent(targetState = page, label = "page") { target ->
                when (target) {
                    "spring" -> SpringSimulation(onBack = { page = "home" })
                    else -> HomeScreen(onOpenSpring = { page = "spring" })
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onOpenSpring: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("EDUSIM", color = Teal, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text("Học bằng cách\nkhám phá.", fontSize = 32.sp, lineHeight = 38.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Phòng mô phỏng khoa học dành cho THPT", color = Muted, fontSize = 15.sp)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Lớp 10", "Lớp 11", "Lớp 12").forEach {
                    Surface(color = Panel, shape = RoundedCornerShape(50)) {
                        Text(it, Modifier.padding(horizontal = 16.dp, vertical = 10.dp), color = androidx.compose.ui.graphics.Color.White)
                    }
                }
            }
        }
        item { Text("MÔ PHỎNG NỔI BẬT", color = Muted, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
        item {
            FeatureCard("VẬT LÝ · LỚP 11", "Dao động điều hòa",
                "Điều chỉnh khối lượng, độ cứng lò xo và quan sát chuyển động.",
                "Mở mô phỏng", onOpenSpring)
        }
        item {
            FeatureCard("TOÁN HỌC · LỚP 10", "Hệ thức lượng tam giác",
                "Mô phỏng hình học tương tác sẽ được bổ sung ở giai đoạn tiếp theo.",
                "Sắp ra mắt", {}, enabled = false)
        }
        item {
            FeatureCard("VẬT LÝ · LỚP 10", "Chuyển động ném xiên",
                "Khảo sát quỹ đạo theo vận tốc đầu, góc ném và trọng trường.",
                "Sắp ra mắt", {}, enabled = false)
        }
        item { Text("BẢN MẪU 0.1.0 · Mô phỏng chạy trên thiết bị", color = Muted, fontSize = 11.sp) }
    }
}

@Composable
fun FeatureCard(category: String, title: String, description: String, action: String, onClick: () -> Unit, enabled: Boolean = true) {
    Surface(
        color = Panel, shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth().clickable(enabled = enabled, onClick = onClick)
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Text(category, color = Teal, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(title, fontSize = 21.sp, fontWeight = FontWeight.SemiBold)
            Text(description, color = Muted, fontSize = 14.sp, lineHeight = 20.sp)
            Text("→  $action", color = if (enabled) Teal else Muted, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SpringSimulation(onBack: () -> Unit) {
    var mass by remember { mutableFloatStateOf(1.0f) }
    var stiffness by remember { mutableFloatStateOf(12.0f) }
    var amplitude by remember { mutableFloatStateOf(0.7f) }
    var running by remember { mutableStateOf(true) }
    var time by remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(running) {
        while (running) {
            delay(16)
            time += 0.016
        }
    }

    val omega = sqrt(stiffness / mass)
    val period = 2 * PI / omega
    val displacement = amplitude * cos(omega * time).toFloat()
    val velocity = (-amplitude * omega * sin(omega * time)).toFloat()
    val acceleration = -omega * omega * displacement
    val energy = 0.5f * stiffness * amplitude * amplitude

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("‹  Quay lại", color = Teal, modifier = Modifier.clickable { onBack() }.padding(8.dp))
            Spacer(Modifier.weight(1f))
            Text("VẬT LÝ 11", color = Muted, fontSize = 12.sp)
        }
        Text("Con lắc lò xo", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Dao động điều hòa · Mô hình lý tưởng không ma sát", color = Muted, fontSize = 13.sp)
        Spacer(Modifier.height(14.dp))
        Surface(color = Panel, shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                SpringCanvas(displacement)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { running = !running }, modifier = Modifier.weight(1f)) {
                        Text(if (running) "Tạm dừng" else "Tiếp tục")
                    }
                    OutlinedButton(onClick = { time = 0.0; running = false }, modifier = Modifier.weight(1f)) { Text("Đặt lại") }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        ParameterSlider("Khối lượng m", mass, 0.2f, 5f, "%.1f kg".format(mass)) { mass = it }
        ParameterSlider("Độ cứng lò xo k", stiffness, 2f, 40f, "%.0f N/m".format(stiffness)) { stiffness = it }
        ParameterSlider("Biên độ A", amplitude, 0.1f, 1f, "%.1f m".format(amplitude)) { amplitude = it }
        Spacer(Modifier.height(8.dp))
        Text("SỐ LIỆU THỜI GIAN THỰC", color = Teal, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Metric("Li độ x", "%.2f m".format(displacement), Modifier.weight(1f))
            Metric("Vận tốc v", "%.2f m/s".format(velocity), Modifier.weight(1f))
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Metric("Gia tốc a", "%.2f m/s²".format(acceleration), Modifier.weight(1f))
            Metric("Chu kỳ T", "%.2f s".format(period), Modifier.weight(1f))
        }
        Text("Cơ năng: %.3f J    ω = %.2f rad/s".format(energy, omega), color = Muted, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        Text("Mô hình: x = A cos(ωt),  ω = √(k/m)", color = androidx.compose.ui.graphics.Color.White, fontSize = 13.sp, modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
fun SpringCanvas(displacement: Float) {
    Canvas(Modifier.fillMaxWidth().height(210.dp)) {
        val midY = size.height * 0.48f
        val anchorX = size.width * 0.12f
        val centerX = size.width * 0.62f
        val blockX = centerX + displacement * size.width * 0.20f
        drawLine(Muted, Offset(anchorX, midY - 55), Offset(anchorX, midY + 55), 4f)
        drawLine(Muted, Offset(anchorX, midY), Offset(size.width * 0.92f, midY), 1f)
        val path = Path()
        val coils = 12
        path.moveTo(anchorX, midY)
        for (i in 1..coils) {
            val x = anchorX + (blockX - anchorX) * i / (coils + 1)
            val y = midY + if (i % 2 == 0) -18f else 18f
            path.lineTo(x, y)
        }
        path.lineTo(blockX, midY)
        drawPath(path, Teal, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f, cap = StrokeCap.Round))
        drawRect(Teal, Offset(blockX, midY - 25), androidx.compose.ui.geometry.Size(48f, 50f))
        drawLine(Muted, Offset(centerX, midY + 45), Offset(centerX, midY + 62), 2f)
        drawLine(Muted, Offset(centerX - 5, midY + 62), Offset(centerX + 5, midY + 62), 2f)
        drawLine(Teal, Offset(centerX, midY + 75), Offset(blockX + 24, midY + 75), 2f)
        drawCircle(Teal, 4f, Offset(blockX + 24, midY + 75))
    }
}

@Composable
fun ParameterSlider(label: String, value: Float, min: Float, max: Float, display: String, onChange: (Float) -> Unit) {
    Column(Modifier.padding(vertical = 2.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = androidx.compose.ui.graphics.Color.White, fontSize = 13.sp)
            Spacer(Modifier.weight(1f))
            Text(display, color = Teal, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Slider(value = value, onValueChange = onChange, valueRange = min..max)
    }
}

@Composable
fun Metric(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(color = Panel, shape = RoundedCornerShape(14.dp), modifier = modifier) {
        Column(Modifier.padding(12.dp)) {
            Text(label, color = Muted, fontSize = 11.sp)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
