package com.wien.microservices.wienservices.poc.streams;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

//@TestMethodOrder(MethodOrderer.MethodName.class)
//@ExtendWith(MockitoExtension.class)
public class StreamsTest {

    private Logger log = LoggerFactory.getLogger(StreamsTest.class);

    private static List<UserTestBean> users;
    private static List<UserTestBean> anotherUsers;

    private static List<List<UserTestBean>> allUsers;

    @BeforeAll
    public static void setUp() {
        users = Arrays.asList(new UserTestBean[]{
                new UserTestBean(2001, "Ankit", LocalDate.parse("1980-12-05")),
                new UserTestBean(2002, "Sameep", LocalDate.parse("1981-10-01")),
                new UserTestBean(2003, "Meghana", LocalDate.parse("1992-11-24")),
                new UserTestBean(2004, "Saurabh", LocalDate.parse("1989-01-01"))
        });

        anotherUsers = Arrays.asList(new UserTestBean[]{
                new UserTestBean(6001, "Rahul", LocalDate.parse("1985-03-05")),
                new UserTestBean(6002, "Rounak", LocalDate.parse("1958-04-02")),
                new UserTestBean(6003, "Arnab", LocalDate.parse("1970-09-05")),
                new UserTestBean(6004, "Thakur", LocalDate.parse("1995-08-25"))
        });

        allUsers = new ArrayList<>();

        allUsers.add(users);
        allUsers.add(anotherUsers);


    }

    @Test
    public void testMap_TypeOfOneListToAnother_Success() throws Exception {
        List<Integer> userIds = users.stream().map(u -> u.getId()).collect(Collectors.toList());
        log.info("User ids");
        userIds.forEach(id -> log.info(String.valueOf(id)));

        assertEquals(users.size(), userIds.size());
    }

    @Test
    public void testFlatMap_Flatten_SuccessWithoutSort() throws Exception {
        List<UserTestBean> users = allUsers.stream().flatMap(l -> l.stream()).collect(Collectors.toList());
        users.forEach(user -> log.info(user.getName()));

        assertEquals(8, users.size());
    }

    @Test
    public void testMatch_AllAnyNone_Success() throws Exception {
        boolean allMatch = users.stream().allMatch(user -> user.getName().contains("b"));
        boolean anyMatch = users.stream().anyMatch(user -> user.getName().contains("b"));
        boolean noneMatch = users.stream().noneMatch(user -> user.getName().contains("b"));
        log.info("allMatch: {}, anyMatch: {}, noneMatch: {}", allMatch, anyMatch, noneMatch);
        assertEquals(false, allMatch);
        assertEquals(true, anyMatch);
        assertEquals(false, noneMatch);
    }

    @Test
    public void testSpecialOps_MaxMinAverageSum_Success() throws Exception {
        List<Integer> numbers = Arrays.asList(new Integer[]{2, 7, 0, 8, 7, 4, 6, 9});
        int sum = 43;
        double average = 43.00 / 8.00;
        int min = 0;
        int max = 9;
        assertEquals(sum, numbers.stream().reduce(Integer::sum).get());
        assertEquals(average, numbers.stream().mapToDouble(n -> n).average().getAsDouble());
//        assertEquals(average, numbers.stream().reduce(Double::));
        assertEquals(min, numbers.stream().reduce(Integer::min).get());
        assertEquals(max, numbers.stream().reduce(Integer::max).get());

        //With IntSummaryStatistics
        IntSummaryStatistics intSummaryStatistics = numbers.stream().mapToInt(n -> n).summaryStatistics();

        assertEquals(sum, intSummaryStatistics.getSum());
        assertEquals(average, intSummaryStatistics.getAverage());
        assertEquals(min, intSummaryStatistics.getMin());
        assertEquals(max, intSummaryStatistics.getMax());
    }

    @Test
    public void testCollect_Joining_JoinSuccess() throws Exception {
        String namesOfUsers = users.stream().map(u -> u.getName()).collect(Collectors.joining());
        log.info(namesOfUsers);
        assertEquals("AnkitSameepMeghanaSaurabh", namesOfUsers);

        //With delimiter
        String namesOfUsersWithDeli = users.stream().map(u -> u.getName()).collect(Collectors.joining(", "));
        log.info(namesOfUsersWithDeli);
        assertEquals("Ankit, Sameep, Meghana, Saurabh", namesOfUsersWithDeli);
    }

    @Test
    public void testCollect_toMap_CollectionSuccess() throws Exception {
        Map<Integer, UserTestBean> userMap = users.stream().collect(Collectors.toMap(l -> l.getId(), l -> l));
        userMap.forEach((k, v) -> log.info("Key: " + k + " " + "Value: " + v));
        assertEquals(4, userMap.keySet().size());
    }

    @Test
    public void testCollect_partitionBy_PartitionSuccess() throws Exception {
        Map<Boolean, List<UserTestBean>> usersWithAge40 = allUsers.stream().flatMap(l -> l.stream())
                .collect(Collectors
                        .partitioningBy(u -> Period.between(u.getDob(), LocalDate.now()).getYears() > 40));
        usersWithAge40.forEach((k, v) -> log.info("Key: " + k + " " + "Value: " + v));
    }

    @Test
    public void testCollect_groupingByVariousAge_groupingSuccess() throws Exception {
        Map<Integer, List<UserTestBean>> usersWithVariousAgeRange = allUsers.stream().flatMap(l -> l.stream())
                .collect(Collectors
                        .groupingBy(u -> Period.between(u.getDob(), LocalDate.now()).getYears()));
        usersWithVariousAgeRange.forEach((k, v) -> log.info("Key: " + k + " " + "Value: " + v));
    }

    @Test
    public void testCollect_groupingByVariousAgeAndMapping_groupingSuccess() throws Exception {
        Map<Integer, List<String>> usersGroup = allUsers.stream().flatMap(l -> l.stream())
                .collect(Collectors
                        .groupingBy(u -> Period.between(u.getDob(), LocalDate.now()).getYears(), Collectors.mapping(UserTestBean::getName, Collectors.toList())));
        usersGroup.forEach((k, v) -> log.info("Key: " + k + " " + "Value: " + v));
    }

    @Test
    public void testPalindrome_fileReadAsStream_Success() throws Exception {
        Files.lines(Paths.get("X:\\MyProjects\\wien-services\\src\\test\\java\\com\\wien\\microservices\\wienservices\\poc\\streams\\pallindrom.txt"))
                .forEach(l -> log.info(l));
    }

    @Test
    public void testFileWrite_WriteWithCharset_Success() throws Exception {
        Files.writeString(Paths.get("./src/test/java/com/wien/microservices/wienservices/poc/streams/someFile.txt"), "SomeThing");
    }

    @Test
    public void testFileWrite_WriteWithStream_Success() throws Exception {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get("./src/test/java/com/wien/microservices/wienservices/poc/streams/someFile1.txt")))) {
            allUsers.stream().flatMap(u -> u.stream()).forEach(pw::println);
        }
    }
}
