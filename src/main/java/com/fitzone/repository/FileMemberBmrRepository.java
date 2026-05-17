package com.fitzone.repository;

import com.fitzone.model.MemberBmr;
import com.fitzone.util.CsvUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FileMemberBmrRepository {

    @Value("${fitzone.data.bmr-file}")
    private String bmrFilePath;

    @PostConstruct
    public void init() {
        CsvUtil.ensureFileExists(bmrFilePath, CsvUtil.BMR_HEADER);
    }

    public void saveBmr(MemberBmr bmr) {
        CsvUtil.appendLine(bmrFilePath, bmr.toCsvLine());
    }

    public List<MemberBmr> getAllBmrRecords() {
        List<String> lines = CsvUtil.readDataLines(bmrFilePath);
        List<MemberBmr> records = new ArrayList<>();
        for (String line : lines) {
            MemberBmr bmr = CsvUtil.parseMemberBmrLine(line);
            if (bmr != null) {
                records.add(bmr);
            }
        }
        return records;
    }

    public List<MemberBmr> findByMemberId(String memberId) {
        return getAllBmrRecords().stream()
                .filter(b -> b.getMemberId().equalsIgnoreCase(memberId))
                .collect(Collectors.toList());
    }

    public Optional<MemberBmr> findLatestByMemberId(String memberId) {
        List<MemberBmr> memberRecords = findByMemberId(memberId);
        if (memberRecords.isEmpty()) {
            return Optional.empty();
        }
        // Since we append, the last one is the latest
        return Optional.of(memberRecords.get(memberRecords.size() - 1));
    }
}
