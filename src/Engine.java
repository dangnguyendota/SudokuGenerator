import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;

public class Engine {
    public static boolean ENABLE_BASIC_REDUCTION = true;
    public static boolean ENABLE_ADVANCE_REDUCTION = true;
    public static boolean ENABLE_PROFESSIONAL_REDUCTION = true;


    public static void start(SudokuLevel level, int nOfLessons, String link) throws IOException {
        int n = nOfLessons;
        GeneratorUtil gen = new GeneratorUtil();
        Random random = new Random();
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(link), StandardCharsets.UTF_8));
        while (n > 0) {
            n--;
            gen.genBoard(level);
            StringBuilder builder = new StringBuilder();
            builder.append(level);
            builder.append(" WITH ID: ");
            builder.append(Long.toHexString(random.nextLong()));
            builder.append("\r\nQUESTION ENCODE: q{");
            builder.append(Objects.requireNonNull(ResultUtil.getBoard()).encode());
            builder.append("}");
            builder.append("\r\nQUESTION: \r\n");
            builder.append(ResultUtil.getBoard());
            builder.append("\r\nANSWER ENCODE: q{");
            builder.append(Objects.requireNonNull(ResultUtil.getAnswer()).encode());
            builder.append("}");
            builder.append("\r\nANSWER: \r\n");
            builder.append(ResultUtil.getAnswer());
            builder.append("\r\n\r\n::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::\r\n\r\n");
            writer.write(builder.toString());
        }
        writer.close();
    }

}
