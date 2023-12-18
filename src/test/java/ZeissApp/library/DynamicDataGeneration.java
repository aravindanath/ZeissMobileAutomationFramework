package ZeissApp.library;

import com.github.javafaker.Faker;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DynamicDataGeneration {


    public static String generateRandomName() {
        Faker faker = new Faker(new Locale("en-IND"));
        String name = faker.name().firstName().trim().replace(".", "");
        return name;
    }


    public static String getYearInt() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        String yy = Integer.toString(year + 2);
        return yy;
    }

    public static String getContent(String url) throws Exception {
        StringBuilder sb = new StringBuilder();
        try {
            for (Scanner sc = new Scanner(new URL(url).openStream()); sc.hasNext(); )
                sb.append(sc.nextLine()).append('\n');
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    private static final String NUMERIC_STRING = "0123456789";

    public static String randomNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * NUMERIC_STRING.length());
            builder.append(NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


    public static String firstName() {
        Faker fake = new Faker(new Locale("en-IND"));
        return fake.name().firstName();

    }

    public static String lastName() {
        Faker fake = new Faker(new Locale("en-IND"));
        return fake.name().lastName();

    }

    public static String emailID() {
        Faker fake = new Faker(new Locale("en-IND"));
        String email = fake.name().fullName();
        return email.replace(" ", "").replace(".", "") + "@lsqdev.in";

    }

    public static String getLeadOrigin() {
        String[] arr = {"Internet", "Referal", "Direct", "Social Media", "InBound", "Organic Search"};
        Random r = new Random();
        int randomNumber = r.nextInt(arr.length);
        return arr[randomNumber];
    }


    public static String getAddress() {
        String[] arr = {"HSR", "BTM", "Whitefield", "Jayanagar", "Marathalli"};
        Random r = new Random();
        int randomNumber = r.nextInt(arr.length);
        return arr[randomNumber];

    }

    public static String getCities() {
        Faker fake = new Faker(new Locale("en-IND"));
        String cities = fake.address().city();
        return cities;
    }

    public static String getNote() {
        Faker fake = new Faker(new Locale("en-IND"));
        String notes = fake.lorem().paragraph(2);
        return notes;
    }


    public static String generateMobileNumber() {
        long x = 1000000000L;
        long y = 9999999999L;

        Random r = new Random();

        long number = x + ((long) (r.nextDouble() * (y - x)));

        System.out.println("Number generated " + number);

        return number + "";
    }
    public static char generateRandomChar() {
        Random r = new Random();
        return (char) (r.nextInt(26) + 'A');
    }

    public static String randomPAN() {
        final String randomString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final Random rand = new Random();
        final Set<String> identifiers = new HashSet<String>();
        int min = 1001;
        int max = 9998;
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        StringBuilder builder = new StringBuilder();
        while (builder.toString().length() == 0) {
            for (int i = 0; i < 5; i++) {
                builder.append(randomString.charAt(rand.nextInt(randomString.length())));
            }
            builder.append(randomNum);
            for (int i = 0; i < 1; i++) {
                builder.append(randomString.charAt(rand.nextInt(randomString.length())));
            }
            if (identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }

    public static String generateAlphaNumeric() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }


    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    private static final String SPECIAL_CHAR_STRING = "#$%^&*())(*&^%$#$%^&";

    public static String randomSpecialChar(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * SPECIAL_CHAR_STRING.length());
            builder.append(SPECIAL_CHAR_STRING.charAt(character));
        }
        return builder.toString();
    }


    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {

        Set<T> set = new LinkedHashSet<>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    public static String parseNumberString(String numberText) {
        String number = "";
        char c;
        for (int i = 0; i < numberText.length(); i++) {
            c = numberText.charAt(i);
            if (Character.isDigit(c) || c == '.')
                number += c;
        }
        return number;
    }



    public String getRandomStringFromArray(String[] arr) {
        Random r = new Random();
        int randomNumber = r.nextInt(arr.length);
        return arr[randomNumber];
    }
    public String getRandomStringFromList(List<String> list) {
        Random r = new Random();
        int randomNumber = r.nextInt(list.size());
        return list.get(randomNumber);
    }
    public static String getCountry() {
        String[] arr = {"India", "Pakistan", "China", "Bolivia", "Brazil"};
        Random r = new Random();
        int randomNumber = r.nextInt(arr.length);
        return arr[randomNumber];

    }

    /**
     * Date format as 2023-05-20 13:21:50 , yyyy-MM-dd HH:mm:ss
     * @param format
     * @return
     */
    public String getCurrentDate(String format)
    {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCompanyName(){
        Faker faker = new Faker(new Locale("en-IND"));
       return faker.company().name();
    }

    public static String getEmailId() {
      return  firstName()+"@testmail.com";
    }



    public static String randomNumeric(float count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * NUMERIC_STRING.length());
            builder.append(NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

}