package com.fitzone.repository;

import com.fitzone.model.Member;
import com.fitzone.util.CsvUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

//File-based repository for Member CRUD operations.
//Reads from and writes to a CSV file (members.txt).
//All write operations are thread-safe via CsvUtil's synchronized methods.

@Repository
public class FileMemberRepository {

    @Value("${fitzone.data.members-file}")
    private String membersFilePath;

//Ensures the members data file exists on application startup.

    @PostConstruct
    public void init() {
        CsvUtil.ensureFileExists(membersFilePath, CsvUtil.MEMBERS_HEADER);
    }

//Saves a new member by appending to the CSV file.

    public void saveMember(Member member) {
        CsvUtil.appendLine(membersFilePath, member.toCsvLine());
    }

//Returns all members from the CSV file.

    public List<Member> getAllMembers() {
        List<String> lines = CsvUtil.readDataLines(membersFilePath);
        List<Member> members = new ArrayList<>();
        for (String line : lines) {
            Member m = CsvUtil.parseMemberLine(line);
            if (m != null) {
                members.add(m);
            }
        }
        return members;
    }

//Finds a member by their ID (e.g. M001).

    public Optional<Member> findById(String memberId) {
        return getAllMembers().stream()
                .filter(m -> m.getMemberId().equalsIgnoreCase(memberId))
                .findFirst();
    }

//Searches members by partial, case-insensitive match on name, email, or ID.

    public List<Member> searchMembers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllMembers();
        }
        String lowerQuery = query.trim().toLowerCase();
        return getAllMembers().stream()
                .filter(m ->
                        m.getMemberId().toLowerCase().contains(lowerQuery) ||
                                m.getFullName().toLowerCase().contains(lowerQuery) ||
                                m.getEmail().toLowerCase().contains(lowerQuery)
                )
                .collect(Collectors.toList());
    }

//Updates an existing member by rewriting the entire file.
//Matches on memberId and replaces the corresponding line.

    public void updateMember(Member updatedMember) {
        List<Member> allMembers = getAllMembers();
        List<String> updatedLines = new ArrayList<>();
        for (Member m : allMembers) {
            if (m.getMemberId().equalsIgnoreCase(updatedMember.getMemberId())) {
                updatedLines.add(updatedMember.toCsvLine());
            } else {
                updatedLines.add(m.toCsvLine());
            }
        }
        CsvUtil.rewriteFile(membersFilePath, CsvUtil.MEMBERS_HEADER, updatedLines);
    }

//Deletes a member by their ID. Rewrites the file without the deleted member.

    public void deleteMember(String memberId) {
        List<Member> allMembers = getAllMembers();
        List<String> remainingLines = allMembers.stream()
                .filter(m -> !m.getMemberId().equalsIgnoreCase(memberId))
                .map(Member::toCsvLine)
                .collect(Collectors.toList());
        CsvUtil.rewriteFile(membersFilePath, CsvUtil.MEMBERS_HEADER, remainingLines);
    }
}
