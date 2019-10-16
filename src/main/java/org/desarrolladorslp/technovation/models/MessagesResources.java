package org.desarrolladorslp.technovation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "messages_resources")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagesResources implements Serializable {

    private static final long serialVersionUID = 6814368125368211389L;

    @Id
    @Column(name = "message_id", nullable = false)
    private UUID messageId;

    @Id
    @Column(name = "resource_id", nullable = false)
    private UUID resourceId;

}
