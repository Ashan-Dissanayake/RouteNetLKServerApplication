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
           'Incident Allocation Test Branch',
           'IAT9001',
           'Test Address',
           '0119000001',
           'test@gmail.com',
           CURRENT_DATE,
           'Incident vehicle allocation repository test branch',
           2,
           1,
           1,
           FALSE
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
           'Incident Allocation Tester',
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
           'incidentallocation9001',
           'test-password',
           FALSE,
           NULL,
           NULL,
           FALSE,
           'Incident vehicle allocation repository test user',
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
           'IAT9001',
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
           'IAT9001',
           1,
           1,
           50000,
           2,
           1,
           1,
           'Incident allocation test vehicle',
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
)
VALUES (
           9001,
           9001,
           9001,
           'IATPERMIT9001',
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
           'Incident allocation test trip',
           2,
           1,
           1,
           9001,
           1
       );

-- ============================================================
-- 8. TRIP EXECUTION
-- ============================================================

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
           'Incident allocation test trip execution',
           9,
           9001
       );

-- ============================================================
-- 9. INCIDENT
-- ============================================================

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
           'Mechanical breakdown - allocation test',
           1,
           9001
       );

-- ============================================================
-- 10. INCIDENT VEHICLE ALLOCATION
-- ============================================================

INSERT INTO incidentvehicleallocation (
    id,
    incident_id,
    vehicle_id,
    providedbranch_id,
    incidentvehicleallocationstatus_id,
    doassigned,
    doreleased,
    user_id,
    branch_id
)
VALUES (
           9001,
           9001,
           9001,
           9001,
           1,
           '2026-08-10 09:30:00',
           NULL,
           9001,
           9001
       );
