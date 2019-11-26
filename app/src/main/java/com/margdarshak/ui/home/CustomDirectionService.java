package com.margdarshak.ui.home;

import com.mapbox.api.directions.v5.DirectionsService;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CustomDirectionService {

    /**
     * Constructs the html get call using the information passed in through the
     * {@link MapboxDirections.Builder}.
     * @param coordinates         the coordinates the route should follow
     * @return the {@link DirectionsResponse} in a Call wrapper
     * @since 1.0.0
     */
    @GET("directions/v1/{profile}/{coordinates}")
    Call<DirectionsResponse> getCall(
            @Path("profile") String profile,
            @Path("coordinates") String coordinates
    );

    /**
     * Constructs the post html call using the information passed in through the
     * {@link MapboxDirections.Builder}.
     *
     * @param userAgent           the user agent
     * @param user                the user
     * @param profile             the profile directions should use
     * @param coordinates         the coordinates the route should follow
     * @param accessToken         Mapbox access token
     * @param alternatives        define whether you want to receive more then one route
     * @param geometries          route geometry
     * @param overview            route full, simplified, etc.
     * @param radiuses            start at the most efficient point within the radius
     * @param steps               define if you'd like the route steps
     * @param bearings            used to filter the road segment the waypoint will be placed on by
     *                            direction and dictates the angle of approach
     * @param continueStraight    define whether the route should continue straight even if the
     *                            route will be slower
     * @param annotations         an annotations object that contains additional details about each
     *                            line segment along the route geometry. Each entry in an
     *                            annotations field corresponds to a coordinate along the route
     *                            geometry
     * @param language            language of returned turn-by-turn text instructions
     * @param roundaboutExits     Add extra step when roundabouts occur with additional information
     *                            for the user
     * @param voiceInstructions   request that the response contain voice instruction information,
     *                            useful for navigation
     * @param bannerInstructions  request that the response contain banner instruction information,
     *                            useful for navigation
     * @param voiceUnits          voice units
     * @param exclude             exclude tolls, motorways or more along your route
     * @param approaches          which side of the road to approach a waypoint
     * @param waypointIndices     which input coordinates should be treated as waypoints/separate legs
     *                            Note: coordinate indices not added here act as silent waypoints
     * @param waypointNames       custom names for waypoints used for the arrival instruction
     * @param waypointTargets     list of coordinate pairs for drop-off locations
     * @param enableRefresh       whether the routes should be refreshable
     * @param walkingSpeed        walking speed
     * @param walkwayBias         a factor that modifies the cost when encountering roads or paths
     *                            that do not allow vehicles and are set aside for pedestrian use
     * @param alleyBias           a factor that modifies the cost when alleys are encountered
     * @return the {@link DirectionsResponse} in a Call wrapper
     * @since 4.6.0
     */
    @FormUrlEncoded
    @POST("directions/v1/{profile}")
    Call<DirectionsResponse> postCall(
            @Header("User-Agent") String userAgent,
            @Path("user") String user,
            @Path("profile") String profile,
            @Field("coordinates") String coordinates,
            @Query("access_token") String accessToken,
            @Field("alternatives") Boolean alternatives,
            @Field("geometries") String geometries,
            @Field("overview") String overview,
            @Field("radiuses") String radiuses,
            @Field("steps") Boolean steps,
            @Field("bearings") String bearings,
            @Field("continue_straight") Boolean continueStraight,
            @Field("annotations") String annotations,
            @Field("language") String language,
            @Field("roundabout_exits") Boolean roundaboutExits,
            @Field("voice_instructions") Boolean voiceInstructions,
            @Field("banner_instructions") Boolean bannerInstructions,
            @Field("voice_units") String voiceUnits,
            @Field("exclude") String exclude,
            @Field("approaches") String approaches,
            @Field("waypoints") String waypointIndices,
            @Field("waypoint_names") String waypointNames,
            @Field("waypoint_targets") String waypointTargets,
            @Field("enable_refresh") Boolean enableRefresh,
            @Field("walking_speed") Double walkingSpeed,
            @Field("walkway_bias") Double walkwayBias,
            @Field("alley_bias") Double alleyBias
    );
}
