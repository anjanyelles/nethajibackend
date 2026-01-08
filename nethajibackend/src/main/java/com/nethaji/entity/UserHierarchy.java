package com.nethaji.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "user_hierarchy")
@Data
public class UserHierarchy
{


    public enum HierarchyType {
        PRINCIPAL_LECTURER,LECTURER_ASSISTANT_LECTURER,SUPERVISOR_STUDENT
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "supervisor_id")
    private UUID supervisorId;


    @Column(name = "subordinate_id")
    private UUID subordinateId;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User supervisor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subordinate_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User subordinate;

    @Enumerated(EnumType.STRING)
    @Column(name = "hierarchy_type")
    private HierarchyType hierarchyType;


    @Column(name = "created_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;





}
