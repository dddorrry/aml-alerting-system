package org.rbc;

import java.time.LocalTime;


record Transaction(LocalTime timestamp, int amount, int accountId) {}