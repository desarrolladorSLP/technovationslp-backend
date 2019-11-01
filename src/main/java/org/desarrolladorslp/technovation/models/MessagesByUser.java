package org.desarrolladorslp.technovation.models;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages_by_users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagesByUser implements Serializable {
    private static final long serialVersionUID = 8147277458435466313L;

    @Id
    @Column(name = "message_id", nullable = false)
    private UUID messageId;

    @Id
    @Column(name = "user_receiver_id", nullable = false)
    private UUID userReceiverId;
}
