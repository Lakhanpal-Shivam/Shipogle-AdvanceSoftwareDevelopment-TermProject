package com.shipogle.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.shipogle.app.model.DashboardFilter;
import com.shipogle.app.model.DriverRoute;
import com.shipogle.app.repository.DriverRouteRepository;
import com.shipogle.app.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class DriverRouteFilterTest {

    @Mock
    private DriverRouteRepository driverRouteRepository;

    @InjectMocks
    private DriverRouteFilter driverRouteFilter;

    final private Long TEST_DRIVER_ROUTE = 2L;

    final private int EXPECTED_FILTER_SIZE = 2;

    @Test
    public void testFilterWithNullRoutes() {
        // given
        List<DriverRoute> driverRoutes = null;

        // when
        List<DriverRoute> filteredRoutes = driverRouteFilter.filter(driverRoutes);

        // then
        assertThat(filteredRoutes).isNull();
    }

    @Test
    public void testFilterWithEmptyRoutes() {
        // given
        List<DriverRoute> driverRoutes = new ArrayList<>();

        // when
        List<DriverRoute> filteredRoutes = driverRouteFilter.filter(driverRoutes);

        // then
        assertThat(filteredRoutes).isEqualTo(driverRoutes);
    }

    @Test
    public void testGetDriverRoutesByFilters() {
        // given
        DashboardFilter filter = new DashboardFilter();
        filter.setSourceCity("New York");
        filter.setDestination("Los Angeles");

        DriverRoute driverRoute1 = new DriverRoute();
        driverRoute1.setDriverRouteId(1L);
        driverRoute1.setSourceCity("New York");
        driverRoute1.setDestinationCity("Los Angeles");

        DriverRoute driverRoute2 = new DriverRoute();
        driverRoute2.setDriverRouteId(TEST_DRIVER_ROUTE);
        driverRoute2.setSourceCity("Chicago");
        driverRoute2.setDestinationCity("Dallas");

        List<DriverRoute> expectedRoutes = Arrays.asList(driverRoute1, driverRoute2);

        when(driverRouteRepository.getDriverRoutesByFilters("New York", "Los Angeles", null, null, null, null, null, null))
                .thenReturn(expectedRoutes);

        // when
        List<DriverRoute> filteredRoutes = driverRouteFilter.getDriverRoutesByFilters(filter);

        // then
        assertThat(filteredRoutes).hasSize(EXPECTED_FILTER_SIZE);
        assertThat(filteredRoutes).containsExactlyInAnyOrderElementsOf(expectedRoutes);
    }

    @Test
    public void testGetDriverRoutesByFilters_NoMatchingRoutes() {
        // given
        DashboardFilter filter = new DashboardFilter();
        filter.setSourceCity("New York");
        filter.setDestination("Los Angeles");

        List<DriverRoute> expectedRoutes = Collections.emptyList();
        when(driverRouteRepository.getDriverRoutesByFilters("New York", "Los Angeles", null, null, null, null, null, null))
                .thenReturn(expectedRoutes);

        // when
        List<DriverRoute> filteredRoutes = driverRouteFilter.getDriverRoutesByFilters(filter);

        // then
        assertThat(filteredRoutes).isEmpty();
    }

    @Test
    public void testGetDriverRoutesByFilters_NullInput() {
        // given
        DashboardFilter filter = null;

        // when
        List<DriverRoute> filteredRoutes = driverRouteFilter.getDriverRoutesByFilters(filter);

        // then
        assertThat(filteredRoutes).isNull();
    }
    @Test
    public void testGetDriverRouteById_NullInput() {
        // given
        when(driverRouteRepository.getDriverRoutes(null)).thenReturn(null);

        // when
        List<DriverRoute> filteredRoutes = driverRouteFilter.getDriverRouteById(null);

        // then
        assertThat(filteredRoutes).isNull();
    }

    @Test
    public void testGetDriverRouteById_NonExistingDriver() {
        // given
        when(driverRouteRepository.getDriverRoutes("3")).thenReturn(Collections.emptyList());

        // when
        List<DriverRoute> filteredRoutes = driverRouteFilter.getDriverRouteById("3");

        // then
        assertThat(filteredRoutes).isEmpty();
    }

    @Test
    public void testFilter_NullInput() {
        // given
        List<DriverRoute> driverRoutes = null;

        // when
        List<DriverRoute> filteredRoutes = driverRouteFilter.filter(driverRoutes);

        // then
        assertThat(filteredRoutes).isNull();
    }
}