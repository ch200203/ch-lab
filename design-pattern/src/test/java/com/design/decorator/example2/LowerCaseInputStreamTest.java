package com.design.decorator.example2;

import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

class LowerCaseInputStreamTest {

    @Test
    void inputTest() throws IOException {
        int c;

        try {
            InputStream in = new LowerCaseInputStream(new LowerCaseInputStream(
                    new BufferedInputStream(
                            new FileInputStream("test.txt")
                    )
            ));

            while ((c = in.read()) >= 0 ) {
                System.out.println((char) c);
            }

            in.close(); // autoCloseable 이 되기떄문에 굳이 필요는 없음
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
