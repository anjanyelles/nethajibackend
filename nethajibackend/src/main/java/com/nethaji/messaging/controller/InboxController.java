package com.nethaji.messaging.controller;

import com.nethaji.messaging.dto.InboxListItem;
import com.nethaji.messaging.dto.UnreadCountResponse;
import com.nethaji.messaging.entity.InboxMessage;
import com.nethaji.messaging.repository.InboxMessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nethaji-service/inbox")
@CrossOrigin
public class InboxController {

    private final InboxMessageRepository inboxMessageRepository;

    public InboxController(InboxMessageRepository inboxMessageRepository) {
        this.inboxMessageRepository = inboxMessageRepository;
    }

    @GetMapping
    public ResponseEntity<List<InboxListItem>> list(@RequestParam UUID userId) {
        List<InboxMessage> list = inboxMessageRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<InboxListItem> out = new ArrayList<>();
        for (InboxMessage m : list) {
            out.add(new InboxListItem(m.getId(), m.getTitle(), m.getBody(), m.getCreatedAt(), m.getReadAt()));
        }
        return ResponseEntity.ok(out);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<UnreadCountResponse> unreadCount(@RequestParam UUID userId) {
        return ResponseEntity.ok(new UnreadCountResponse(inboxMessageRepository.countByUserIdAndReadAtIsNull(userId)));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<InboxMessage> markRead(@PathVariable UUID id) {
        InboxMessage m = inboxMessageRepository.findById(id).orElse(null);
        if (m == null) {
            return ResponseEntity.notFound().build();
        }
        if (m.getReadAt() == null) {
            m.setReadAt(new Date());
            inboxMessageRepository.save(m);
        }
        return ResponseEntity.ok(m);
    }
}
