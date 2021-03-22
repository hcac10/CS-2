package edu.caltech.cs2.lab03;

import edu.caltech.cs2.libraries.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Image {
    private Pixel[][] pixels;

    public Image(File imageFile) throws IOException {
        BufferedImage img = ImageIO.read(imageFile);
        this.pixels = new Pixel[img.getWidth()][img.getHeight()];
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                this.pixels[i][j] = Pixel.fromInt(img.getRGB(i, j));
            }
        }
    }

    private Image(Pixel[][] pixels) {
        this.pixels = pixels;
    }

    public Image transpose() {
        Pixel[][] temp = new Pixel[this.pixels[0].length][this.pixels.length];
        for(int i = 0; i < this.pixels[0].length; ++i)
            for(int j = 0; j < this.pixels.length; ++j)
                temp[i][j] = this.pixels[j][i];

        Image ans = new Image(temp);
        return ans;
    }

    public String decodeText() {
        ArrayList<Integer> ans = new ArrayList<>();
        ArrayList<Character> temp = new ArrayList<>();
        for(int i = 0; i < this.pixels.length; ++i) {
            for (int j = 0; j < this.pixels[0].length; ++j) {
                ans.add(this.pixels[i][j].getLowestBitOfR());
                if(ans.size() == 8){
                    String tem = "";
//                    int val = 0;
                    for(int k = 0; k < 8; ++k) {
                        tem += ans.get(7-k);
                    }
                    int value = Integer.parseInt(tem, 2);
                    if(value != 0) {
                        temp.add((char) value);
                    }
                    ans.clear();
                }
            }
        }
        String answer = "";
        for(char c: temp)
            answer += c;
        return answer;
    }

    public Image hideText(String text) {
        char[] temp = text.toCharArray();
        ArrayList<String> bin = new ArrayList<>();
        ArrayList<Integer> total = new ArrayList<>();
        for(char c: temp){
            int i = c;
            bin.add(decToBinary(i));
        }
        Pixel[][] ans = this.pixels;
        for(String str: bin) {
            for (int i = 0; i < 8; ++i) {
                if (str.charAt(7-i) == '1')
                    total.add(1);
                else
                    total.add(0);
            }
        }
        for(int i = 0; i < ans.length; ++i)
            for(int j = 0; j < ans[0].length; ++j)
                if(i*ans[0].length + j < total.size())
                    ans[i][j] = ans[i][j].fixLowestBitOfR(total.get(i*ans[0].length + j));
                else
                    ans[i][j] = ans[i][j].fixLowestBitOfR(0);
        Image sol = new Image(ans);
        return sol;
    }

    public String decToBinary(int i){
        String ans = "";
        while(i != 0) {
            if (i % 2 == 0)
                ans += 0;
            else
                ans += 1;
            i /= 2;
        }
        String answer = "";
        for(int m = 0; m < ans.length(); ++m)
            answer += ans.charAt(ans.length() - 1 - m);
        while(answer.length() != 8)
            answer = "0" + answer;
        return answer;
    }

    public BufferedImage toBufferedImage() {
        BufferedImage b = new BufferedImage(this.pixels.length, this.pixels[0].length, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < this.pixels.length; i++) {
            for (int j = 0; j < this.pixels[0].length; j++) {
                b.setRGB(i, j, this.pixels[i][j].toInt());
            }
        }
        return b;
    }

    public void save(String filename) {
        File out = new File(filename);
        try {
            ImageIO.write(this.toBufferedImage(), filename.substring(filename.lastIndexOf(".") + 1, filename.length()), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
