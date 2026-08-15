-- ============================================================
-- 1. BRANCH
-- ============================================================

INSERT INTO branch (
    id,
    name,
    code,
    address,
    telephone,
    email,
    docreated,
    remarks,
    branchtype_id,
    branchstatus_id,
    regionaloffice_id,
    deleted
)
VALUES (
    9001,
    'Incident Repository Test Branch',
    'TST9001',
    'Test Address',
    '0119000001',
    'branch9001@test.com',
    CURRENT_DATE,
    'Incident repository test branch',
    2,
    1,
    1,
    false
);


-- ============================================================
-- 2. EMPLOYEE
-- ============================================================

INSERT INTO employee (
    id,
    number,
    fullname,
    callingname,
    nic,
    mobile,
    email,
    address,
    emergencycontact,
    doj,
    gender_id,
    branch_id,
    department_id,
    designation_id,
    employeetype_id,
    employeestatus_id,
    deleted
)
VALUES (
    9001,
    'EMP9001',
    'Incident Repository Tester',
    'Tester',
    '900000001V',
    '0779000001',
    'employee9001@test.com',
    'Test Address',
    '0719000001',
    CURRENT_DATE,
    1,
    9001,
    1,
    4,
    1,
    1,
    FALSE
);


-- ============================================================
-- 3. USER
-- ============================================================

INSERT INTO `user` (
    id,
    username,
    password,
    accountlocked,
    recoverycode,
    recoverycodeexpiration,
    recoverycodeused,
    remarks,
    employee_id,
    usertype_id,
    userstatus_id
)
VALUES (
    9001,
    'incidenttest9001',
    'test-password',
    FALSE,
    NULL,
    NULL,
    FALSE,
    'Incident repository test user',
    9001,
    1,
    1
);


-- ============================================================
-- 4. ROUTE
-- ============================================================

INSERT INTO route (
    id,
    number,
    origin,
    destination,
    distancekm,
    routetype_id,
    mingapminutes,
    waypoints,
    requiredroutefamiliaritylevel_id
)
VALUES (
    9001,
    'RT9001',
    'Pettah',
    'Gampaha',
    35.0,
    1,
    10,
    NULL,
    1
);


-- ============================================================
-- 5. VEHICLE
-- ============================================================

INSERT INTO vehicle (
    id,
    branch_id,
    number,
    model_id,
    bustype_id,
    mileage,
    fueltype_id,
    conditionrate_id,
    vehiclestatus_id,
    remarks,
    deleted,
    user_id
)
VALUES (
    9001,
    9001,
    'TST9001',
    1,
    1,
    50000,
    2,
    1,
    1,
    'Incident repository test vehicle',
    FALSE,
    9001
);


-- ============================================================
-- 6. PERMIT
-- ============================================================

INSERT INTO permite (
    id,
    branch_id,
    route_id,
    number,
    vehicle_id,
    doissued,
    doexpired,
    notripsperday,
    permitestatus_id,
    servicetype_id,
    deleted,
    user_id
) VALUES (
             9001,
             9001,
             9001,
             'PERMIT9001',
             9001,
             '2026-01-01',
             '2027-01-01',
             10,
             1,
             1,
             FALSE,
             9001
         );


-- ============================================================
-- 7. TRIP
-- ============================================================

INSERT INTO trip (
    id,
    branch_id,
    triptype_id,
    permite_id,
    todepature,
    toarrival,
    breakminutes,
    remarks,
    tripstatus_id,
    originterminal_id,
    opcalender_id,
    user_id,
    shift_id
)
VALUES (
    9001,
    9001,
    1,
    9001,
    '08:00:00',
    '10:00:00',
    10,
    'Incident repository test trip',
    2,
    1,
    1,
    9001,
    1
);


-- ============================================================
-- 8. TRIP EXECUTION - MONDAY COMPLETED
-- ============================================================
-- 2026-08-10 = Monday
-- status 9 = Completed

INSERT INTO tripexecution (
    id,
    branch_id,
    trip_id,
    vehicle_id,
    driver_id,
    conductor_id,
    doservice,
    toactualdeparture,
    toactualarrival,
    startodometer,
    endodometer,
    passengercount,
    tripno,
    remarks,
    tripexecutionstatus_id,
    user_id
)
VALUES (
    9001,
    9001,
    9001,
    9001,
    NULL,
    NULL,
    '2026-08-10',
    '08:00:00',
    '10:00:00',
    10000,
    10050,
    50,
    1,
    'Completed Monday trip',
    9,
    9001
);


-- ============================================================
-- 9. TRIP EXECUTION - TUESDAY COMPLETED
-- ============================================================
-- 2026-08-11 = Tuesday
-- status 9 = Completed

INSERT INTO tripexecution (
    id,
    branch_id,
    trip_id,
    vehicle_id,
    driver_id,
    conductor_id,
    doservice,
    toactualdeparture,
    toactualarrival,
    startodometer,
    endodometer,
    passengercount,
    tripno,
    remarks,
    tripexecutionstatus_id,
    user_id
)
VALUES (
    9002,
    9001,
    9001,
    9001,
    NULL,
    NULL,
    '2026-08-11',
    '08:00:00',
    '10:00:00',
    10050,
    10100,
    45,
    2,
    'Completed Tuesday trip',
    9,
    9001
);


-- ============================================================
-- 10. TRIP EXECUTION - WEDNESDAY SCHEDULED
-- ============================================================
-- Used to prove getTripsCountByDay(9) only counts Completed.
-- 2026-08-12 = Wednesday
-- status 1 = Scheduled

INSERT INTO tripexecution (
    id,
    branch_id,
    trip_id,
    vehicle_id,
    driver_id,
    conductor_id,
    doservice,
    toactualdeparture,
    toactualarrival,
    startodometer,
    endodometer,
    passengercount,
    tripno,
    remarks,
    tripexecutionstatus_id,
    user_id
)
VALUES (
    9003,
    9001,
    9001,
    9001,
    NULL,
    NULL,
    '2026-08-12',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    3,
    'Scheduled Wednesday trip',
    1,
    9001
);


-- ============================================================
-- 11. INCIDENT - MECHANICAL BREAKDOWN - MONDAY
-- ============================================================
-- type 1 = Mechanical Breakdown
-- status 1 = Reported
--
-- Should be included in:
-- getIncidentsCountByDay(1)

INSERT INTO incident (
    id,
    branch_id,
    tripexecution_id,
    incidenttype_id,
    regionalarea_id,
    toreported,
    doreported,
    odometeratincident,
    remarks,
    incidentstatus_id,
    user_id
)
VALUES (
    9001,
    9001,
    9001,
    1,
    1,
    '09:15:00',
    '2026-08-10',
    10025,
    'Mechanical breakdown - Monday',
    1,
    9001
);


-- ============================================================
-- 12. INCIDENT - MECHANICAL BREAKDOWN - TUESDAY
-- ============================================================
-- type 1 = Mechanical Breakdown
--
-- Should also be included in:
-- getIncidentsCountByDay(1)

INSERT INTO incident (
    id,
    branch_id,
    tripexecution_id,
    incidenttype_id,
    regionalarea_id,
    toreported,
    doreported,
    odometeratincident,
    remarks,
    incidentstatus_id,
    user_id
)
VALUES (
    9002,
    9001,
    9002,
    1,
    1,
    '09:30:00',
    '2026-08-11',
    10075,
    'Mechanical breakdown - Tuesday',
    2,
    9001
);


-- ============================================================
-- 13. INCIDENT - ACCIDENT - MONDAY
-- ============================================================
-- type 2 = Accident

INSERT INTO incident (
    id,
    branch_id,
    tripexecution_id,
    incidenttype_id,
    regionalarea_id,
    toreported,
    doreported,
    odometeratincident,
    remarks,
    incidentstatus_id,
    user_id
)
VALUES (
    9003,
    9001,
    9001,
    2,
    1,
    '09:45:00',
    '2026-08-10',
    10030,
    'Accident - Monday',
    4,
    9001
);


-- ============================================================
-- 14. INCIDENT - TYRE PUNCTURE - TUESDAY
-- ============================================================
-- type 3 = Tyre Puncture
-- status 5 = Resolved
--
-- Used to verify that findActiveIncidentsByBranch()
-- excludes Resolved incidents.

INSERT INTO incident (
    id,
    branch_id,
    tripexecution_id,
    incidenttype_id,
    regionalarea_id,
    toreported,
    doreported,
    odometeratincident,
    remarks,
    incidentstatus_id,
    user_id
)
VALUES (
    9004,
    9001,
    9002,
    3,
    1,
    '10:00:00',
    '2026-08-11',
    10080,
    'Resolved tyre puncture',
    5,
    9001
);


-- ============================================================
-- 15. INCIDENT - PENDING ALLOCATION
-- ============================================================
-- status 4 = Pending Allocation
--
-- This MUST be counted by:
-- countPendingIncidentsByBranch(9001)

INSERT INTO incident (
    id,
    branch_id,
    tripexecution_id,
    incidenttype_id,
    regionalarea_id,
    toreported,
    doreported,
    odometeratincident,
    remarks,
    incidentstatus_id,
    user_id
)
VALUES (
    9005,
    9001,
    9003,
    3,
    1,
    '11:00:00',
    '2026-08-12',
    NULL,
    'Pending allocation incident',
    4,
    9001
);

