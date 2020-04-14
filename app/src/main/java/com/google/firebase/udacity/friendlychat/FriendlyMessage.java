/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.firebase.udacity.friendlychat;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class FriendlyMessage {

    private String name;
    private String mUsername;

    private List<String> photoUrl =new ArrayList<>();
    public List<Pair<String, String>> mReviews;

    public FriendlyMessage() {
    }

    public FriendlyMessage( String name, List<String> photoUrl, String Review, String username) {
        this.name = name;
        this.photoUrl=photoUrl;
        this.mUsername=username;
        mReviews.add(new Pair<String, String>(username,Review));
    }
    public String getName() {
        return name;
    }


    public List<String> getPhotoUrl() {
        return photoUrl;
    }

    public String getmUsername() {
        return mUsername;
    }

    public List<Pair<String, String>> getmReviews() {
        return mReviews;
    }

    public void addReview(String username, String review)
    {
        mReviews.add(new Pair<String, String>(username,review));
    }
}
